package com.example.dzivekodywallet.data.util

import android.content.Context
import com.example.dzivekodywallet.viewmodel.WalletsViewModelFactory
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.AppDatabase

object Injection {
   fun provideWalletsViewModelFactory(context: Context): WalletsViewModelFactory {
       val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao)

       return WalletsViewModelFactory(walletRepository)
   }
}