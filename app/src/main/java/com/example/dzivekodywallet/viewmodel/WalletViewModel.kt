package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.*
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    lateinit var balances: LiveData<List<Balance>>
    lateinit var transactions: LiveData<List<Transaction>>

    private var _walletId = MutableLiveData<Long>()
    val walletId: LiveData<Long>
        get() = _walletId

    private var _walletName = MutableLiveData<String>()
    val walletName: LiveData<String>
        get() = _walletName

    private var _walletPublicKey = MutableLiveData<String>()
    val walletPublicKey: LiveData<String>
        get() = _walletPublicKey

    private var _wallet = MutableLiveData<Wallet>()
    val wallet: LiveData<Wallet>
        get() = _wallet

    private val _selectedTransaction = MutableLiveData<Transaction>()
    val selectedTransaction: LiveData<Transaction>
        get() = _selectedTransaction

    val operations: LiveData<List<Operation>> = selectedTransaction.switchMap { tr ->
        walletRepository.getOperationsForTransaction(tr.transactionId)
    }

    init {}

    fun setWalletId(walletId: Long) {
        _walletId.value = walletId
        // when WalletFragment sets walletId, get balances for given wallet
        balances = walletRepository.getBalances(walletId)
        viewModelScope.launch {
            transactions = walletRepository.getTransactions(walletId)
        }
    }

    fun setSelectedTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }

    fun updateBalance() {
        viewModelScope.launch {
            walletRepository.syncBalancesFromNetwork(walletId.value!!)
        }
    }

    fun updateTransactions() {
        viewModelScope.launch {
            walletRepository.syncTransactionsFromNetwork(walletId.value!!)
        }
    }

    fun updateName() {
        viewModelScope.launch {
            _walletName.value = walletRepository.getWallet(walletId.value!!)?.name
        }
    }

    fun updatePublicKey() {
        viewModelScope.launch {
            _walletPublicKey.value = walletRepository.getWallet(walletId.value!!)?.publicKey
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
