package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository

class WalletsViewModelFactory(private val walletRepository: WalletRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WalletsViewModel(walletRepository) as T
//        return super.create(modelClass)
    }
}