package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
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

    suspend fun generateNewWallet(walletName: String) {
        withContext(Dispatchers.IO) {
//            val nextWalletNumber = walletDao.getAllWallets().value?.size
//            Log.d("KOKOT", nextWalletNumber.toString())
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName + (1..80).random()
            wallet.balance = 0.0
            // TODO:
            // zasifrovat
            wallet.privateKey = generatedKeyPair.secretSeed.toString()
            wallet.publicKey = generatedKeyPair.publicKey.toString()
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