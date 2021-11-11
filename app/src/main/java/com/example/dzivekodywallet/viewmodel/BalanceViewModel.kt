package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.blockchain.StellarService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BalanceViewModel(walletId: Long, private var walletRepository: WalletRepository,
                       private var stellarService: StellarService
) : ViewModel() {

    private var _balance = MutableLiveData<Double>()
    val balance: LiveData<Double>
        get() = _balance

    init {
        _balance.value = 0.0
    }

    private suspend fun updateBalanceAsync() : Double {
        return withContext(Dispatchers.IO) {
            val acc = stellarService.getAccountInformation("GAVYM6ZIKHGBOVR3QJA7WWY3ANU5Z5PWAHMTM7F33SRZZZONP3Q5US6W")
            val balanceFromBlockchain = stellarService.getBalance(acc)
            Log.d("BalanceViewModel", balanceFromBlockchain.toString())
            return@withContext balanceFromBlockchain!!
        }
    }

    fun updateBalance() {
        viewModelScope.launch {
            _balance.value = updateBalanceAsync()
        }
    }

}