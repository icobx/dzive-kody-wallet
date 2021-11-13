package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository
import java.lang.IllegalArgumentException

class BalanceViewModelFactory(private val walletId: Long, private val walletRepository: WalletRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(walletId, walletRepository) as T
        } else {
            throw IllegalArgumentException("Unable to create BalanceViewModel")
        }
    }
}