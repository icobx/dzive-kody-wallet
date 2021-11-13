package com.example.dzivekodywallet.data.blockchain

import android.util.Log
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse

// TODO:
// rename to:
//      TransactionService
//      StellarService
class StellarService private constructor() {
    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

    fun getAccountInformation(accountId: String) : AccountResponse? {
        return try {
            blockchainServer.accounts().account(accountId)
        } catch(e: Exception) {
            null
        }
    }

    fun generateAccount(): KeyPair {
        return KeyPair.random()
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
        private var INSTANCE: StellarService? = null

        fun getInstance(): StellarService {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = StellarService()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}