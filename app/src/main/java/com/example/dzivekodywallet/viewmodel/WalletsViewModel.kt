package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.*
import org.stellar.sdk.Server
import org.stellar.sdk.xdr.AssetType
import java.util.*

data class WalletsViewModel(private val walletRepository: WalletRepository): ViewModel() {

    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

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
//        viewModelScope.launch {
//            _allWallets.value = walletRepository.getAllWallets()?.value
//        }
    }

    fun insertWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.insertWallet(wallet)

//            withContext(Dispatchers.Default) {
                getBalanceAsync()
//            }
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.deleteWallet(wallet)
        }
    }

    private suspend fun getBalanceAsync() {
        return coroutineScope {

            val account = blockchainServer.accounts().account("GAVYM6ZIKHGBOVR3QJA7WWY3ANU5Z5PWAHMTM7F33SRZZZONP3Q5US6W")

            for (balance in account.balances) {
                if (balance.assetType.equals("native"))
                {
                    Log.d("REPO", balance.balance.toString())
                }
            }
        }
    }
}
