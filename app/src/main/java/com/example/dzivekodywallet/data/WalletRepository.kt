package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
import com.example.dzivekodywallet.data.util.Encryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    fun getWallet(key: Long): Wallet? {
        return walletDao.getWallet(key)
    }

    fun getAllWallets(): LiveData<List<Wallet>> {
        return walletDao.getAllWallets()
    }

    fun synchronizeWallet(wallet: Wallet): Wallet? {
        // TODO:
        // rework
        val accountInfo = stellarService.getAccountInformation(wallet.publicKey)
        if (accountInfo != null) {
            wallet.balance = accountInfo.balances[0].balance?.toDouble()!!
            return wallet
        }

        return null
    }

    suspend fun generateNewWallet(walletName: String, secretPhrase: String) {
        withContext(Dispatchers.IO) {
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName + (1..80).random()
            wallet.balance = 0.0
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
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