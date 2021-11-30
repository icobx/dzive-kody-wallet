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
import org.stellar.sdk.requests.RequestBuilder
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import org.stellar.sdk.requests.TooManyRequestsException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.ReadWriteProperty


class StellarService private constructor() {
    private val blockchainServer: Server = Server("https://horizon-testnet.stellar.org")


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
        val accountInfo: AccountResponse
        return try{
            accountInfo = blockchainServer.accounts().account(accountId)
            out.addAll(accountInfo.balances)
            Error.NO_ERROR
        }  catch (e: TooManyRequestsException) {
            Error.NO_ERROR
        } catch (e: Exception) {
            Error.ERROR_STELLAR
        }
    }

    fun makeTransaction(srcId: String, destId: String, amount: String): Error {
        val source: KeyPair
        val sourceAccount: AccountResponse
        val destinationAccount: AccountResponse
        try {
            source  = KeyPair.fromSecretSeed(srcId)
            sourceAccount = blockchainServer.accounts().account(source.accountId)
        } catch(e: Exception) {
            return Error.ERROR_INVALID_SOURCE_ACCOUNT
        }

        try {
            destinationAccount = blockchainServer.accounts().account(destId)
        } catch(e: Exception) {
            return Error.ERROR_INVALID_DESTINATION_ACCOUNT
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
            if (response.isSuccess) {
                Error.NO_ERROR
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            Error.ERROR_TRANSACTION_SUBMIT
        }
    }

    fun getTransactions(accountId: String, transactions: MutableList<TransactionResponse>) : Error {
        return try {
            var tsPage: Page<TransactionResponse> = blockchainServer
                .transactions()
                .forAccount(accountId)
                .includeFailed(true)
                .limit(50)
                .order(RequestBuilder.Order.DESC)
                .execute()

            while (tsPage.records.size > 0) {
                transactions.addAll(tsPage.records)
                tsPage = tsPage.getNextPage(blockchainServer.httpClient)
            }

            Error.NO_ERROR
        }  catch (e: TooManyRequestsException) {
            Error.NO_ERROR
        } catch (e: Exception) {
            Error.ERROR_STELLAR
        }
    }

    fun getOperationsForTransaction(transactionId: String, operations: MutableList<OperationResponse>): Error {
        return try {
            var operationsPerTransaction = blockchainServer
                .operations()
                .forTransaction(transactionId)
                .limit(50)
                .order(RequestBuilder.Order.DESC)
                .execute()

            while (operationsPerTransaction.records.size > 0) {
                operations.addAll(operationsPerTransaction.records)
                operationsPerTransaction = operationsPerTransaction
                    .getNextPage(blockchainServer.httpClient)
            }

            Error.NO_ERROR
        } catch (e: TooManyRequestsException) {
            Error.NO_ERROR
        } catch (e: Exception) {
            Error.ERROR_STELLAR
        }
    }

    fun getOperations(accountId: String, operations: MutableList<OperationResponse>): Error {
        return try {
            var paymentsPage = blockchainServer
                .payments()
                .forAccount(accountId)
                .limit(50)
                .order(RequestBuilder.Order.DESC)
                .execute()

                while (paymentsPage.records.size > 0) {
                    operations.addAll(paymentsPage.records)
                    paymentsPage = paymentsPage.getNextPage(blockchainServer.httpClient)
                }

            Error.NO_ERROR
        } catch (e: TooManyRequestsException) {
            Error.NO_ERROR
        }catch (e: Exception) {
            Error.ERROR_STELLAR
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