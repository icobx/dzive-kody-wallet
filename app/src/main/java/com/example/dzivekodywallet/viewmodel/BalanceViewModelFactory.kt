package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import java.lang.IllegalArgumentException

class BalanceViewModelFactory(private val walletId: Long, private val walletRepository: WalletRepository, private val stellarService: StellarService) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(walletId, walletRepository, stellarService) as T
        } else {
            throw IllegalArgumentException("Unable to create BalanceViewModel")
        }
    }
}