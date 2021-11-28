package com.example.dzivekodywallet.data.blockchain

import android.util.Log
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.Page
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.TransactionResponse
import org.stellar.sdk.responses.operations.OperationResponse
import java.io.InputStream
import com.example.dzivekodywallet.data.util.Error
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.ReadWriteProperty


class StellarService private constructor() {
    private var blockchainServer: Server = Server("https://horizon-testnet.stellar.org")

//    fun getAccountInformation(accountId: String) : AccountResponse? {
//        return try {
//        } catch(e: Exception) {
//            null
//        }
//    }

    fun generateAccount() : Pair<Error, KeyPair> {
        val newKeyPair = KeyPair.random()

        val friendBotUrl = String.format(
            "https://friendbot.stellar.org/?addr=%s",
            newKeyPair.accountId
        )

        try {
            URL(friendBotUrl).openStream()
        } catch(e: Exception) {
            return Pair(Error.ERROR_GENERATING_ACCOUNT, newKeyPair)
        }

        return Pair(Error.NO_ERROR, newKeyPair)
    }

    fun getBalance(accountId: String, out: MutableList<AccountResponse.Balance> ) : Error {
        val temp: AccountResponse
        return try{
            temp = blockchainServer.accounts().account(accountId)
            out.addAll(temp.balances)
            Error.NO_ERROR
        } catch (e: Exception) {
            Error.ERROR_STELLAR
        }
    }

    fun makeTransaction(srcId: String, destId: String, amount: String): Boolean {
        val source: KeyPair
        val sourceAccount: AccountResponse
        val destinationAccount: AccountResponse
        try {
            source  = KeyPair.fromSecretSeed(srcId)
            sourceAccount = blockchainServer.accounts().account(source.accountId)
            destinationAccount = blockchainServer.accounts().account(destId)
        } catch(e: Exception) {
            Log.d("PVALOG", "Unable to get an account: $destId")
            return false
        }

        val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
            .addOperation(
                PaymentOperation.Builder(
                    destinationAccount.accountId,
                    AssetTypeNative(),
                    amount
                ).build()
            )
            .setTimeout(180) // Set the amount of lumens you're willing to pay per operation to submit your transaction
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build()

        transaction.sign(source)

        return try {
            val response: SubmitTransactionResponse = blockchainServer.submitTransaction(transaction)
            Log.d("transaction","Success: ${response.isSuccess}")
            response.isSuccess
        } catch (e: java.lang.Exception) {
            Log.d("Transaction Failed: ", e.message.toString())
            false
        }
    }

    fun getTransactions(accountId: String) : ArrayList<TransactionResponse> {
        return try {
            val tsPage: Page<TransactionResponse> = blockchainServer
                .transactions()
                .forAccount(accountId)
                .limit(50)
                .execute()

            tsPage.records
        } catch (e: Exception) {
            ArrayList()
        }
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