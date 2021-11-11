package com.example.dzivekodywallet.data.blockchain

import android.util.Log
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse

class Transactions private constructor() {
    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

    fun getAccountInformation(accountId: String) : AccountResponse {
        return blockchainServer.accounts().account(accountId)
    }

    fun generateAccount() : AccountResponse
    {
        val keyPair = KeyPair.random()

        return getAccountInformation(keyPair.accountId)
    }

    fun getBalance(account: AccountResponse) : Double? {
        for (balance in account.balances) {
            if (balance.assetType.equals("native")) {
                return balance.balance.toDouble()
            }
        }
        return null
    }

    companion object {
        @Volatile
        private var INSTANCE: Transactions? = null

        fun getInstance(): Transactions {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Transactions()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}