package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository

class WalletViewModelFactory(private val walletRepository: WalletRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            return WalletViewModel(walletRepository) as T
        } else {
            throw IllegalArgumentException("Unable to create WalletViewModel")
        }
    }
}
