package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.*
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.util.Error
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {
    lateinit var balances: LiveData<List<Balance>>
    lateinit var transactions: LiveData<List<Transaction>>
    lateinit var payments: LiveData<List<Operation>>
    var error: LiveData<Error> = walletRepository.error

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

    fun setWalletId(walletId: Long) {
        _walletId.value = walletId
        // when WalletFragment sets walletId, get balances for given wallet
        balances = walletRepository.getBalances(walletId)
        viewModelScope.launch {
            transactions = walletRepository.getTransactions(walletId)
            payments = walletRepository.getPayments(walletId)
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

    fun updatePayments() {
        viewModelScope.launch {
            walletRepository.syncOperations(walletId.value!!)
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

    fun makeTransaction(destId: String, amount: String, userInput: String, callback: () -> Unit) {
        viewModelScope.launch {
            walletRepository.makeTransaction(walletId.value!!, destId, amount, userInput)
            if (error.value == Error.NO_ERROR)
                callback()
        }
    }

    fun synchronise() {
        viewModelScope.launch {
            walletRepository.synchronise(walletId.value!!)
        }
    }
}
