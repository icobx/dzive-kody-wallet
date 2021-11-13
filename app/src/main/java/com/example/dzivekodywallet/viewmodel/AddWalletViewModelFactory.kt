package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository
import java.lang.IllegalArgumentException

class AddWalletViewModelFactory(private val walletRepository: WalletRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddWalletViewModel::class.java)) {
            return AddWalletViewModel(walletRepository) as T
        } else {
            throw IllegalArgumentException("Unable to create BalanceViewModel")
        }
    }
}