package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    private var _balance = MutableLiveData<Double>()
    val balance: LiveData<Double>
        get() = _balance

    private var _walletId = MutableLiveData<Long>()
    val walletId: LiveData<Long>
        get() = _walletId

    init {
        _balance.value = 0.0
    }

    fun setWalletId(walletId: Long) {
        _walletId.value = walletId
    }

    fun updateBalance() {
        viewModelScope.launch {
            _balance.value = walletRepository.getBalance(_walletId.value!!)
        }
    }
}
