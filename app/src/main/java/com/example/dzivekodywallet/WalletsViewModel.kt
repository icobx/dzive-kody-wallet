package com.example.dzivekodywallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Wallet

data class WalletsViewModel(private val walletRepository: WalletRepository): ViewModel() {
    fun getWallets(): LiveData<List<Wallet>> {
        return walletRepository.getAllWallets()
    }

    fun insertWallet(wallet: Wallet) {
        walletRepository.insertWallet(wallet)
    }
}
