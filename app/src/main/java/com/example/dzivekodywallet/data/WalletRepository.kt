package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WalletRepository private constructor(private val walletDao: WalletDao) {

    fun insertWallet(wallet: Wallet) {
        Log.d("wallet repository", "insert wallet")
        walletDao.insertWallet(wallet)
    }

    fun updateWallet(wallet: Wallet) {
        walletDao.updateWallet(wallet)
    }

    fun getWallet(key: Long): Wallet? {
        return walletDao.getWallet(key)
    }

    fun getAllWallets(): LiveData<List<Wallet>> {
            return walletDao.getAllWallets()
    }

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null


        fun getInstance(walletDao: WalletDao): WalletRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = WalletRepository(walletDao)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}