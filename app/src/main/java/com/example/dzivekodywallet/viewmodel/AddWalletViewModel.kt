package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.util.Error
import kotlinx.coroutines.CompletionHandlerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class AddWalletViewModel(private val walletRepository: WalletRepository): ViewModel() {
    var error: LiveData<Error> = walletRepository.error
    val errors: LiveData<String> = walletRepository.error.map { tr ->
        tr.toString()
    }


    fun addWallet(walletName: String, walletSecretSeed: String, secretPhrase: String,
                  isGeneratingEnabled: Boolean,
                  handler: (publicKey: String, privateKey: String) -> Unit,
                  callback: () -> Unit)
    {
        if (isGeneratingEnabled) {
            viewModelScope.launch {
                val newAccount = walletRepository.generateNewWallet(walletName, secretPhrase)
                if (!newAccount.isNullOrEmpty()) {
                    val public = newAccount.getValue("public_key")
                    val private = newAccount.getValue("private_key")
                    handler(public, private)
                }
            }
        } else {
            viewModelScope.launch {
                val newWalletId = walletRepository.addExistingWallet(
                    walletName,
                    walletSecretSeed,
                    secretPhrase
                )

                if (newWalletId > -1) {
                    walletRepository.syncBalancesFromNetwork(newWalletId)
                    callback()
                }
            }
        }
    }
}
