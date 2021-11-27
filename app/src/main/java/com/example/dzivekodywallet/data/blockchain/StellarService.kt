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

    fun makeTransaction(srcId: String, destId: String, amount: String): Boolean {
        val source = KeyPair.fromSecretSeed(srcId)
        try {
            blockchainServer.accounts().account(destId)
        } catch(e: Exception) {
            Log.d("PVALOG", "Unable to get an account: $destId")
            return false
        }

        val sourceAccount: AccountResponse
        try {
            sourceAccount = blockchainServer.accounts().account(source.accountId)
        } catch (e: Exception) {
            Log.d("PVALOG", "Unable to get an account: ${source.accountId}")
            return false
        }

        val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
            .addOperation(
                PaymentOperation.Builder(
                    destId,
                    AssetTypeNative(),
                    amount
                ).build()
            )
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
        } catch (e: java.lang.Exception) {
            Log.d("Transaction Failed: ", e.message.toString())
            return false
        }
        return true
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