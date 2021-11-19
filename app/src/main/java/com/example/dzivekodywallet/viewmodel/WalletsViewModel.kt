package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.*

data class WalletsViewModel(private val walletRepository: WalletRepository): ViewModel() {

    private var _allWallets = MutableLiveData<List<Wallet>>()
    val allWallets: LiveData<List<Wallet>>
        get() = _allWallets

//    fun getWallets() {
//        (viewModelScope as CoroutineScope).launch {
//            _allWallets.value = walletRepository.getAllWallets().value
//        }
//    }
    fun getWallets(): LiveData<List<Wallet>> {
        return walletRepository.getAllWallets()
    }

    fun insertWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.insertWallet(wallet)
        }
    }

    fun updateWalletStatus(walletId:Long) {
        viewModelScope.launch {
            walletRepository.syncBalancesFromNetwork(walletId)
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.deleteWallet(wallet)
        }
    }
}
