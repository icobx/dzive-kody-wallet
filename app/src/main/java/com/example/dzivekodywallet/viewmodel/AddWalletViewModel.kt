package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Wallet
import javax.crypto.SecretKey

class AddWalletViewModel(private val walletRepository: WalletRepository): ViewModel() {

    fun addExistingWallet(name: String, accountId: String, privateKey: String) {
        val wallet = Wallet()
        wallet.name = name
        wallet.publicKey = accountId
        // encrypt
        wallet.privateKey = privateKey
        wallet.balance = 0.0
        val syncedWallet = walletRepository.synchronizeWallet(wallet)
        if (syncedWallet != null) {
            walletRepository.insertWallet(wallet)
        }

        // TODO
        // display error in UI

    }
}
