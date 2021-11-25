package com.example.dzivekodywallet.data.blockchain

import android.util.Log
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.Page
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.TransactionResponse
import org.stellar.sdk.responses.operations.OperationResponse
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class StellarService private constructor() {
    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

    fun getAccountInformation(accountId: String) : AccountResponse? {
        return try {
            blockchainServer.accounts().account(accountId)
        } catch(e: Exception) {
            null
        }
    }

    fun generateAccount() : KeyPair {
        val pair = KeyPair.random()

        val friendBotUrl = String.format(
            "https://friendbot.stellar.org/?addr=%s",
            pair.accountId
        )
        val response: InputStream = URL(friendBotUrl).openStream()
        val body: String = Scanner(response, "UTF-8").useDelimiter("\\A").next()
        println("SUCCESS! You have a new account :)\n$body")

        return pair
    }

    fun getBalance(accountId: String) : Array<out AccountResponse.Balance>? {
        return getAccountInformation(accountId)?.balances
    }

    fun makeTransaction(srcId: String, destId: String, amount: String) {
        val source = KeyPair.fromSecretSeed(srcId)
        val destination = KeyPair.fromAccountId(destId)
        // First, check to make sure that the destination account exists.
        // You could skip this, but if the account does not exist, you will be charged
        // the transaction fee when the transaction fails.
        // It will throw HttpResponseException if account does not exist or there was another error.
        try {
            blockchainServer.accounts().account(destination.accountId)
        } catch(e: Exception) {
            Log.d("PVALOG", "Unable to get an account: ${e.message}")
            return
        }

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

    fun getTransactions(accountId: String) : ArrayList<TransactionResponse> {
        val tsPage: Page<TransactionResponse> = blockchainServer
            .transactions()
            .forAccount(accountId)
            .limit(50)
            .execute()

        val test: Page<OperationResponse> = blockchainServer
            .operations()
            .forTransaction(tsPage.records[0].hash)
            .execute()

        Log.d("JFLOG", "IN stellarService.getTransactions: ${test.records[0].id.toString()}")

        return tsPage.records
    }

    fun getOperations(transactionId: String): ArrayList<OperationResponse> {
        val operationsPerTransaction = blockchainServer
            .operations()
            .forTransaction(transactionId)
            .limit(50)
            .execute()

        return operationsPerTransaction.records
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