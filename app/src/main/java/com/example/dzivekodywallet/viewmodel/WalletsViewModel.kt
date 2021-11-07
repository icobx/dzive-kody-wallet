package com.example.dzivekodywallet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.model.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.stellar.sdk.Server
import org.stellar.sdk.xdr.AssetType
import java.util.*

data class WalletsViewModel(private val walletRepository: WalletRepository): ViewModel() {

    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

    fun getWallets(): LiveData<List<Wallet>> {
        return walletRepository.getAllWallets()
    }

    fun insertWallet(wallet: Wallet) {
        walletRepository.insertWallet(wallet)

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getBalanceAsync()
            }
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
