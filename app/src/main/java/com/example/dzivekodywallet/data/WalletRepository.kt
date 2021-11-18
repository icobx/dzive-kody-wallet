package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
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

class WalletRepository private constructor(private val walletDao: WalletDao, private val balanceDao: BalanceDao, private val stellarService: StellarService) {

    fun insertWallet(wallet: Wallet): Long {
        Log.d("wallet repository", "insert wallet")
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

    private suspend fun getBalanceFromDatabase(walletId: Long): List<Balance> {
        return withContext(Dispatchers.IO) {
            return@withContext balanceDao.getBalancesForWallet(walletId)
        }
    }

    private fun getAssetName(accountBalance: AccountResponse.Balance): String {
        return if (accountBalance.assetType.equals("native")) {
            "XLM"
        } else {
            if (accountBalance.assetCode.isPresent) {
                accountBalance.assetCode.get().toString()
            } else {
                "-" // TODO: rozhodnut, co s neznamym assetom
            }
        }
    }

    suspend fun syncBalanceFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)
        withContext(Dispatchers.IO) {
            val incomingBalances = stellarService.getBalance(accountId)
            incomingBalances?.forEach { new ->
                val assetName = getAssetName(new)
                Log.d("PVALOG", new.balance.toString())
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

    private suspend fun getAccountIdFromWalletId(walletId: Long): String {
        return withContext(Dispatchers.Default) {
            return@withContext getWallet(walletId)?.publicKey!!
        }
    }

    suspend fun getBalances(walletId: Long): List<Balance> {
        val balances = getBalanceFromDatabase(walletId)
        if (balances.isEmpty()) {
            syncBalanceFromNetwork(walletId)
        }
        return balances
    }

    suspend fun generateNewWallet(walletName: String, secretPhrase: String) {
        withContext(Dispatchers.IO) {
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
            insertWallet(wallet)
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
