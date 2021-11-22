package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.dzivekodywallet.data.blockchain.StellarService
import com.example.dzivekodywallet.data.database.BalanceDao
import com.example.dzivekodywallet.data.database.OperationDao
import com.example.dzivekodywallet.data.database.TransactionDao
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.database.WalletDao
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.util.Encryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.TransactionResponse
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse

class WalletRepository private constructor(
    private val walletDao: WalletDao,
    private val balanceDao: BalanceDao,
    private val transactionDao: TransactionDao,
    private val operationDao: OperationDao,
    private val stellarService: StellarService
) {

//    fun getOperationsForTransaction(transactionId: String)
//        = operationDao.getOperationsForTransaction(transactionId)
//            .switchMap { opList ->
//                liveData {
//                    emit(opList)
//                }
//            }

    fun getOperationsForTransaction(transactionId: String): LiveData<List<Operation>> {
        return operationDao.getOperationsForTransaction(transactionId)
    }


    fun insertWallet(wallet: Wallet): Long {
        return walletDao.insertWallet(wallet)
    }

    fun updateWallet(wallet: Wallet) {
        walletDao.updateWallet(wallet)
    }

    fun deleteWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet)
    }

    suspend fun getWallet(key: Long): Wallet? {
        return withContext(Dispatchers.IO) {
            return@withContext walletDao.getWallet(key)
        }
    }

    fun getAllWallets(): LiveData<List<Wallet>> {
        return walletDao.getAllWallets()
    }

    private fun pairSecretSeedWithPublicKey(secretSeed: String): String {
        return KeyPair.fromSecretSeed(secretSeed).accountId
    }

    suspend fun makeTransaction(srcId: Long, destId: String, amount: String) {
        withContext(Dispatchers.IO) {
        // TODO: FIX SECRET PHrase
            val srcPk = Encryption.decrypt(getWallet(srcId)!!.privateKey, "1234")
            stellarService.makeTransaction(srcPk!!, destId, amount)
        }
    }

    fun getBalances(walletId: Long): LiveData<List<Balance>> {
        return balanceDao.getBalances(walletId)
    }

    suspend fun getTransactions(walletId: Long): LiveData<List<Transaction>> {
        val sourceAccountId = getAccountIdFromWalletId(walletId)
        return transactionDao.getTransactions(sourceAccountId)
    }

    private fun getAssetName(accountBalance: AccountResponse.Balance): String {
        return if (accountBalance.assetType.equals("native")) {
            Log.d("JFLOG", "NATIVE")
            "XLM"
        } else {
            if (accountBalance.assetCode.isPresent) {
                Log.d("JFLOG", accountBalance.assetCode.get().toString())
                accountBalance.assetCode.get().toString()
            } else {
                "-" // TODO: rozhodnut, co s neznamym assetom
            }
        }
    }

    suspend fun syncBalancesFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)

        withContext(Dispatchers.IO) {
            val incomingBalances = stellarService.getBalance(accountId)
            incomingBalances?.forEach { new ->
                val assetName = getAssetName(new)
                val balance = balanceDao.findAssetForWallet(walletId, assetName)
                if (null != balance) {
                    balance.amount = new.balance.toDouble()
                    balanceDao.updateBalance(balance)
                } else {
                    val newBalance = Balance()
                    newBalance.amount = new.balance.toDouble()
                    newBalance.walletId = walletId
                    newBalance.assetName = assetName

                    balanceDao.insertBalance(newBalance)
                }
            }
        }
    }

    private suspend fun syncOperationsForTransaction(transactionId: String) {
        withContext(Dispatchers.IO) {
            val operations = mutableListOf<Operation>()
            val operationResponses = stellarService.getOperations(transactionId)
            operationResponses.forEach { op ->
                val operation = Operation(
                    operationId = op.id,
                    transactionId = transactionId
                )

                operation.operationType = op.type
                when (operation.operationType) {
                    "payment" -> {
                        op as PaymentOperationResponse
                        operation.destinationAccount = op.to
                        operation.amount = op.amount
                        operations.add(operation)
                    }
                    "create_account" -> {
                        op as CreateAccountOperationResponse
                        operation.destinationAccount = op.account
                        operation.amount = op.startingBalance
                        operations.add(operation)
                    }
                    else -> { }
                }
            }

            operationDao.insertOperations(*operations.toTypedArray())
        }
    }

    suspend fun syncTransactionsFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)

        withContext(Dispatchers.IO) {
            val transactionResponses: ArrayList<TransactionResponse> = stellarService
                .getTransactions(accountId)
            val transactions: ArrayList<Transaction> = ArrayList()

            transactionResponses.forEach { tr ->
                val t = Transaction(
                    tr.hash, tr.isSuccessful, tr.sourceAccount, tr.operationCount, tr.createdAt
                )
//                t.createdAt = tr.createdAt
//                t.operationCount = tr.operationCount
//                t.sourceAccountId = tr.sourceAccount
//                t.successful = tr.isSuccessful

                transactions.add(t)
                syncOperationsForTransaction(t.transactionId)
            }

            transactionDao.insertTransactions(*transactions.toTypedArray())
        }
    }

    suspend fun synchronise(walletId: Long) {
        // TODO: call all sync methods for given wallet here
        // this will be called when user requests sync
        syncBalancesFromNetwork(walletId)
        syncTransactionsFromNetwork(walletId)
    }

    private suspend fun getAccountIdFromWalletId(walletId: Long): String {
        return withContext(Dispatchers.Default) {
            return@withContext getWallet(walletId)?.publicKey!!
        }
    }

    suspend fun generateNewWallet(walletName: String, secretPhrase: String) {
        withContext(Dispatchers.IO) {
            val generatedKeyPair = stellarService.generateAccount()
            val wallet = Wallet()
            wallet.name = walletName
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
            insertWallet(wallet)
        }
    }

    suspend fun addExistingWallet(walletName: String, walletSecretSeed: String, secretPhrase: String): Long {
        return withContext(Dispatchers.IO) {
            val wallet = Wallet()
            wallet.name = walletName
            wallet.publicKey = pairSecretSeedWithPublicKey(walletSecretSeed)
            wallet.privateKey = Encryption
                .encrypt(walletSecretSeed, secretPhrase)
                .toString()
            return@withContext insertWallet(wallet)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null


        fun getInstance(
            walletDao: WalletDao,
            balanceDao: BalanceDao,
            transactionDao: TransactionDao,
            operationDao: OperationDao,
            stellarService: StellarService
        ): WalletRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = WalletRepository(walletDao, balanceDao, transactionDao, operationDao, stellarService)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
