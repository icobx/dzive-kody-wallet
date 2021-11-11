package com.example.dzivekodywallet.data.util

import android.content.Context
import com.example.dzivekodywallet.viewmodel.WalletsViewModelFactory
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.Transactions
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.viewmodel.BalanceViewModelFactory

object Injection {
   fun provideWalletsViewModelFactory(context: Context): WalletsViewModelFactory {
       val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao)

       return WalletsViewModelFactory(walletRepository)
   }

    fun provideBalanceViewModelFactory(context: Context): BalanceViewModelFactory {
        val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao)

        return BalanceViewModelFactory(1L, walletRepository, Transactions.getInstance())
    }
}