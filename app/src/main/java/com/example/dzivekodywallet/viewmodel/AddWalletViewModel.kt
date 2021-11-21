package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Contact
import kotlinx.coroutines.CompletionHandlerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class AddWalletViewModel(private val walletRepository: WalletRepository): ViewModel() {

    fun addWallet(walletName: String, walletSecretSeed: String, secretPhrase: String,
                  isGeneratingEnabled: Boolean,
                  handler: (publicKey: String, privateKey: String) -> Unit)
    {
        if (isGeneratingEnabled) {
            viewModelScope.launch {
                val newAccount = walletRepository.generateNewWallet(walletName, secretPhrase)
                val public = newAccount.getValue("public_key")
                val private = newAccount.getValue("private_key")
                handler(public, private)
            }
        } else {
            viewModelScope.launch {
                val newWalletId = walletRepository.addExistingWallet(
                    walletName,
                    walletSecretSeed,
                    secretPhrase
                )
                walletRepository.syncBalancesFromNetwork(newWalletId)
            }
        }
    }
}
