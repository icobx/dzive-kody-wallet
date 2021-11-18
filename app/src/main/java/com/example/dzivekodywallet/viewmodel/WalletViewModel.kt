package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    private var _balances = MutableLiveData<List<Balance>>()
    val balances: LiveData<List<Balance>>
        get() = _balances

    private var _walletId = MutableLiveData<Long>()
    val walletId: LiveData<Long>
        get() = _walletId

    private var _walletName = MutableLiveData<String>()
    val walletName: LiveData<String>
        get() = _walletName

    private var _wallet = MutableLiveData<Wallet>()
    val wallet: LiveData<Wallet>
        get() = _wallet

    init {
        _balances.value = listOf()
    }

    fun setWalletId(walletId: Long) {
        _walletId.value = walletId
    }

    fun updateBalance() {
        viewModelScope.launch {
            _balances.value = walletRepository.getBalances(_walletId.value!!)
        }
    }

    fun updateName() {
        viewModelScope.launch {
            _walletName.value = walletRepository.getWallet(_walletId.value!!)?.name
        }
    }

    fun makeTransaction(destId: String, amount: String) {
        viewModelScope.launch {
            walletRepository.makeTransaction(walletId.value!!, destId, amount)
        }
    }
}
