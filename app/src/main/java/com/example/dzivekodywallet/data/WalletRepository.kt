package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.BalanceDao
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.util.Encryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import org.stellar.sdk.responses.AccountResponse

class WalletRepository private constructor(
    private val walletDao: WalletDao,
    private val balanceDao: BalanceDao,
    private val stellarService: StellarService
) {

    fun insertWallet(wallet: Wallet): Long {
        return walletDao.insertWallet(wallet)
    }

    fun updateWallet(wallet: Wallet) {
        walletDao.updateWallet(wallet)
    }

    fun deleteWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet)
    }

    suspend fun getWallet(key: Long): Wallet? {
        return withContext(Dispatchers.IO) {
            return@withContext walletDao.getWallet(key)
        }
    }

    fun getAllWallets(): LiveData<List<Wallet>> {
        return walletDao.getAllWallets()
    }

    private fun pairSecretSeedWithPublicKey(secretSeed: String): String {
        return KeyPair.fromSecretSeed(secretSeed).accountId
    }

    suspend fun makeTransaction(srcId: Long, destId: String, amount: String) {
        withContext(Dispatchers.IO) {
        // TODO: FIX SECRET PHrase
            val srcPk = Encryption.decrypt(getWallet(srcId)!!.privateKey, "1234")
            stellarService.makeTransaction(srcPk!!, destId, amount)
        }
    }

//    suspend fun getBalanceFromDatabase(walletId: Long): LiveData<List<Balance>> {
//        return withContext(Dispatchers.IO) {
//            return@withContext balanceDao.getBalancesForWallet(walletId)
//        }
//    }
    fun getBalances(walletId: Long): LiveData<List<Balance>> {
        return balanceDao.getBalances(walletId)
    }

    private fun getAssetName(accountBalance: AccountResponse.Balance): String {
        return if (accountBalance.assetType.equals("native")) {
            Log.d("JFLOG", "NATIVE")
            "XLM"
        } else {
            if (accountBalance.assetCode.isPresent) {
                Log.d("JFLOG", accountBalance.assetCode.get().toString())
                accountBalance.assetCode.get().toString()
            } else {
                "-" // TODO: rozhodnut, co s neznamym assetom
            }
        }
    }

    suspend fun syncBalancesFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)
        withContext(Dispatchers.IO) {
            val incomingBalances = stellarService.getBalance(accountId)
            incomingBalances?.forEach { new ->
                val assetName = getAssetName(new)
                Log.d("JFLOG", "IN syncBalance; balance: ${new.balance.toString()}")
                val balance = balanceDao.findAssetForWallet(walletId, assetName)
                if (null != balance) {
                    balance.amount = new.balance.toDouble()
                    balanceDao.updateBalance(balance)
                } else {
                    val newBalance = Balance()
                    newBalance.amount = new.balance.toDouble()
                    newBalance.walletId = walletId
                    newBalance.assetName = assetName

                    balanceDao.insertBalance(newBalance)
                }
            }
        }
    }

    suspend fun synchronise(walletId: Long) {
        // TODO: call all sync methods for given wallet here
        // this will be called when user requests sync
        syncBalancesFromNetwork(walletId)
    }

    private suspend fun getAccountIdFromWalletId(walletId: Long): String {
        return withContext(Dispatchers.Default) {
            return@withContext getWallet(walletId)?.publicKey!!
        }
    }

    // TODO: might not be needed
//    suspend fun getBalances(walletId: Long): List<Balance> {
//        val balances = getBalanceFromDatabase(walletId)
//        if (balances.isEmpty()) {
//            syncBalanceFromNetwork(walletId)
//        }
//        return balances
//    }

    suspend fun generateNewWallet(walletName: String, secretPhrase: String): HashMap<String,String> {
        return withContext(Dispatchers.IO) {
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
            insertWallet(wallet)
            val x = HashMap<String, String>()
            x.put("public_key", wallet.publicKey)
            x.put("private_key", String(generatedKeyPair.secretSeed))
            return@withContext x
        }
    }

    suspend fun addExistingWallet(walletName: String, walletSecretSeed: String, secretPhrase: String): Long {
        return withContext(Dispatchers.IO) {
            val wallet = Wallet()
            wallet.name = walletName
            wallet.publicKey = pairSecretSeedWithPublicKey(walletSecretSeed)
            wallet.privateKey = Encryption
                .encrypt(walletSecretSeed, secretPhrase)
                .toString()
            return@withContext insertWallet(wallet)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null


        fun getInstance(walletDao: WalletDao, balanceDao: BalanceDao, stellarService: StellarService): WalletRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = WalletRepository(walletDao, balanceDao, stellarService)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
