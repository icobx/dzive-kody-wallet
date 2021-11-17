package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddWalletViewModel(private val walletRepository: WalletRepository): ViewModel() {

    fun addWallet(walletName: String, walletSecretSeed: String, secretPhrase: String, isGeneratingEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isGeneratingEnabled) {
                walletRepository.generateNewWallet(walletName, secretPhrase)
            } else {
                walletRepository.addExistingWallet(
                    walletName,
                    walletSecretSeed,
                    secretPhrase
                )
            }
        }
    }
}
