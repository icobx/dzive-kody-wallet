package com.example.dzivekodywallet.data.blockchain

import android.util.Log
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse


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
    
    fun makeTransaction(srcId: String, destId: String, amount: String) {
        val source = KeyPair.fromSecretSeed(srcId)
        val destination = KeyPair.fromAccountId(destId)
        // First, check to make sure that the destination account exists.
        // You could skip this, but if the account does not exist, you will be charged
        // the transaction fee when the transaction fails.
        // It will throw HttpResponseException if account does not exist or there was another error.
        blockchainServer.accounts().account(destination.accountId)

        // If there was no error, load up-to-date information on your account.
        val sourceAccount: AccountResponse = blockchainServer.accounts().account(source.accountId)

        // Start building the transaction.
        val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
            .addOperation(
                PaymentOperation.Builder(
                    destination.accountId,
                    AssetTypeNative(),
                    amount
                ).build()
            ) // A memo allows you to add your own metadata to a transaction. It's
            // optional and does not affect how Stellar treats the transaction.
            .addMemo(Memo.text("Test Transaction")) // Wait a maximum of three minutes for the transaction
            .setTimeout(180) // Set the amount of lumens you're willing to pay per operation to submit your transaction
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build()
        // Sign the transaction to prove you are actually the person sending it.
        transaction.sign(source)

        // And finally, send it off to Stellar!
        try {
            val response: SubmitTransactionResponse = blockchainServer.submitTransaction(transaction)
            Log.d("transaction","Success!")
//            println(response)
        } catch (e: java.lang.Exception) {
            Log.d("transaction", e.message.toString())
//            println(e.message)
            // If the result is unknown (no response body, timeout etc.) we simply resubmit
            // already built transaction:
            // SubmitTransactionResponse response = blockchainServer.submitTransaction(transaction);
        }
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