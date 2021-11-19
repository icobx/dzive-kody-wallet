package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    lateinit var balances: LiveData<List<Balance>>

    private var _walletId = MutableLiveData<Long>()
    val walletId: LiveData<Long>
        get() = _walletId

    private var _walletName = MutableLiveData<String>()
    val walletName: LiveData<String>
        get() = _walletName

    private var _wallet = MutableLiveData<Wallet>()
    val wallet: LiveData<Wallet>
        get() = _wallet

    init {}

    fun setWalletId(walletId: Long) {
        _walletId.value = walletId
        // when WalletFragment sets walletId, get balances for given wallet
        balances = walletRepository.getBalances(walletId)
    }

    fun updateBalance() {
        Log.d("JFLOG", "IN updateBalance()")
        viewModelScope.launch {
            Log.d("JFLOG", "walletRepository.syncBalancesFromNetwork(_walletId.value!!)")
            walletRepository.syncBalancesFromNetwork(walletId.value!!)
        }
    }

    fun updateName() {
        viewModelScope.launch {
            _walletName.value = walletRepository.getWallet(walletId.value!!)?.name
        }
    }

    fun makeTransaction(destId: String, amount: String) {
        viewModelScope.launch {
            walletRepository.makeTransaction(walletId.value!!, destId, amount)
        }
    }

    fun synchronise() {
        viewModelScope.launch {
            walletRepository.synchronise(walletId.value!!)
        }
    }
}
