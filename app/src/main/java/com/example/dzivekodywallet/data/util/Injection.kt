package com.example.dzivekodywallet.data.util

import android.content.Context
import com.example.dzivekodywallet.data.ContactRepository
import com.example.dzivekodywallet.viewmodel.WalletsViewModelFactory
import com.example.dzivekodywallet.viewmodel.ContactsViewModelFactory
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.viewmodel.AddWalletViewModelFactory
import com.example.dzivekodywallet.viewmodel.WalletViewModelFactory
import com.example.dzivekodywallet.viewmodel.SendReceiveViewModelFactory

object Injection {
   fun provideWalletsViewModelFactory(context: Context): WalletsViewModelFactory {
       val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, AppDatabase.getInstance(context).balanceDao, AppDatabase.getInstance(context).transactionDao, StellarService.getInstance())

       return WalletsViewModelFactory(walletRepository)
   }

    fun provideAddWalletViewModelFactory(context: Context): AddWalletViewModelFactory {
        val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, AppDatabase.getInstance(context).balanceDao, AppDatabase.getInstance(context).transactionDao, StellarService.getInstance())

        return AddWalletViewModelFactory(walletRepository)
    }

    fun provideWalletViewModelFactory(context: Context): WalletViewModelFactory {
        val walletRepository = WalletRepository.getInstance(AppDatabase.getInstance(context).walletDao, AppDatabase.getInstance(context).balanceDao, AppDatabase.getInstance(context).transactionDao, StellarService.getInstance())

        return WalletViewModelFactory(walletRepository)
    }

    fun provideContactsViewModelFactory(context: Context): ContactsViewModelFactory {
        val contactRepository = ContactRepository.getInstance(AppDatabase.getInstance(context).contactDao)

        return ContactsViewModelFactory(contactRepository)
    }

    fun provideSendReceiveViewModelFactory(context: Context): SendReceiveViewModelFactory {
        val sendReceiveRepository = ContactRepository.getInstance(AppDatabase.getInstance(context).contactDao)

        return SendReceiveViewModelFactory(sendReceiveRepository)
    }
}