package com.example.dzivekodywallet.data.util

import android.content.Context
import com.example.dzivekodywallet.viewmodel.WalletsViewModelFactory
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.viewmodel.AddWalletViewModelFactory
import com.example.dzivekodywallet.viewmodel.BalanceViewModelFactory

object Injection {
   fun provideWalletsViewModelFactory(context: Context): WalletsViewModelFactory {
       val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, StellarService.getInstance())

       return WalletsViewModelFactory(walletRepository)
   }

    fun provideBalanceViewModelFactory(context: Context): BalanceViewModelFactory {
        val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, StellarService.getInstance())

        return BalanceViewModelFactory(1L, walletRepository, StellarService.getInstance())
    }

    fun provideAddWalletViewModelFactory(context: Context): AddWalletViewModelFactory {
        val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, StellarService.getInstance())

        return AddWalletViewModelFactory(walletRepository)
    }
}