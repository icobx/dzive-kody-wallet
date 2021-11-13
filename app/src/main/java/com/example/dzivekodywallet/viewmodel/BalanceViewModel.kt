package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BalanceViewModel(private val walletId: Long, private val walletRepository: WalletRepository) : ViewModel() {

    private var _balance = MutableLiveData<Double>()
    val balance: LiveData<Double>
        get() = _balance

    init {
        _balance.value = 0.0
    }

//    private suspend fun updateBalanceAsync() : Double {
//        return withContext(Dispatchers.IO) {
//            val acc = stellarService.getAccountInformation("GAVYM6ZIKHGBOVR3QJA7WWY3ANU5Z5PWAHMTM7F33SRZZZONP3Q5US6W")
//            val balanceFromBlockchain = acc?.let { stellarService.getBalance(it) }
//            Log.d("BalanceViewModel", balanceFromBlockchain.toString())
//            return@withContext balanceFromBlockchain!!
//        }
//    }

    fun updateBalance() {
//        viewModelScope.launch {
//            _balance.value = updateBalanceAsync()
//        }
    }

    fun getBalance(accountId: String) {
        viewModelScope.launch {
            walletRepository.getWallet(walletId)
        }
    }
}