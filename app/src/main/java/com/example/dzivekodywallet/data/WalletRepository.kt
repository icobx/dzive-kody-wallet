package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
import com.example.dzivekodywallet.data.util.Encryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import java.lang.Math.random

class WalletRepository private constructor(private val walletDao: WalletDao, private val stellarService: StellarService) {

    fun insertWallet(wallet: Wallet) {
        Log.d("wallet repository", "insert wallet")
        walletDao.insertWallet(wallet)
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


    private fun pairWallet(secretSeed: String): Wallet {
        val keyPair = KeyPair.fromSecretSeed(secretSeed)

        val accountInfo = stellarService.getAccountInformation(keyPair.accountId)
        val wallet = Wallet()
        wallet.publicKey = keyPair.accountId
        wallet.balance = accountInfo?.balances?.get(0)?.balance?.toDouble()!!

        return wallet
    }

    suspend fun makeTransaction(srcId: Long, destId: String, amount: String) {
        withContext(Dispatchers.IO) {
        // TODO: FIX SECRET PHrase
            val srcPk = Encryption.decrypt(getWallet(srcId)!!.privateKey, "1234")
            stellarService.makeTransaction(srcPk!!, destId, amount)
        }
    }


    // TODO: opytat sa ako na toto
    // tahat z db wallet alebo takto po jednom attrib
    suspend fun getBalance(walletId: Long): Double {
        return withContext(Dispatchers.IO) {
            val wallet = getWallet(walletId)
            val accId = wallet?.publicKey
            val acc = stellarService.getAccountInformation(accId!!)
            val newBalance = acc?.let { stellarService.getBalance(it) }
            if (newBalance != null) {
                wallet.balance = newBalance
                updateWallet(wallet)
            }
            return@withContext wallet.balance
        }
    }

    suspend fun generateNewWallet(walletName: String, secretPhrase: String) {
        withContext(Dispatchers.IO) {
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName
            wallet.balance = 0.0
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
            insertWallet(wallet)
        }
    }

    suspend fun addExistingWallet(walletName: String, walletSecretSeed: String, secretPhrase: String) {
        withContext(Dispatchers.IO) {
            val wallet = pairWallet(walletSecretSeed)
            wallet.name = walletName
            wallet.privateKey = Encryption
                .encrypt(walletSecretSeed, secretPhrase)
                .toString()
            insertWallet(wallet)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null


        fun getInstance(walletDao: WalletDao, stellarService: StellarService): WalletRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = WalletRepository(walletDao, stellarService)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}