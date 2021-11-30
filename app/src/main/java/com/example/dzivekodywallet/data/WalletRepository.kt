package com.example.dzivekodywallet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
import com.example.dzivekodywallet.data.util.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.TransactionResponse
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse

class WalletRepository private constructor(
    private val walletDao: WalletDao,
    private val balanceDao: BalanceDao,
    private val transactionDao: TransactionDao,
    private val operationDao: OperationDao,
    private val stellarService: StellarService
) {
    private var _error = MutableLiveData<Error>()
    val error: LiveData<Error>
        get() = _error


    fun getOperationsForTransaction(transactionId: String): LiveData<List<Operation>> {
        return operationDao.getOperationsForTransaction(transactionId)
    }

    fun getPayments(walletId: Long): LiveData<List<Operation>> {
        return operationDao.getPayments(walletId)
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

    suspend fun makeTransaction(sourceWalletId: Long, destId: String, amount: String, userInput: String) {
        withContext(Dispatchers.IO) {
            val wallet = getWallet(sourceWalletId)
            if (wallet == null) {
                _error.postValue(Error.ERROR_WALLET_NOT_FOUND)
                return@withContext
            }
            val srcPk = Encryption.decrypt(wallet.privateKey, userInput)
            if (srcPk == null) {
                _error.postValue(Error.ERROR_SECRET_KEY_DECRYPT)
                return@withContext
            }
            if (wallet.publicKey != KeyPair.fromSecretSeed(srcPk).accountId) {
                _error.postValue(Error.ERROR_BAD_PIN)
                return@withContext
            }
            _error.postValue(stellarService.makeTransaction(srcPk, destId, amount))
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
            "XLM"
        } else {
            if (accountBalance.assetCode.isPresent) {
                accountBalance.assetCode.get().toString()
            } else {
                "-" // TODO: rozhodnut, co s neznamym assetom
            }
        }
    }

    suspend fun syncBalancesFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)

        withContext(Dispatchers.IO) {
            val incomingBalances = mutableListOf<AccountResponse.Balance>()
            _error.postValue(stellarService.getBalance(accountId, incomingBalances))
            incomingBalances.forEach { new ->
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

    // TODO: original
//    private suspend fun syncOperationsForTransaction(transactionId: String) {
//        withContext(Dispatchers.IO) {
//            val operations = mutableListOf<Operation>()
//            val operationResponses = mutableListOf<OperationResponse>()
//            _error.postValue(stellarService.getOperationsForTransaction(transactionId, operationResponses))
//            operationResponses.forEach { op ->
////                op as PaymentOperationResponse
//                val operation = Operation(
//                    operationId = op.id,
//                    transactionId = transactionId
//                )
//
//                operation.operationType = op.type
//                when (operation.operationType) {
//                    "payment" -> {
//                        op as PaymentOperationResponse
//                        operation.destinationAccount = op.to
//                        operation.amount = op.amount
//                        operations.add(operation)
//                    }
//                    "create_account" -> {
//                        op as CreateAccountOperationResponse
//                        operation.destinationAccount = op.account
//                        operation.amount = op.startingBalance
//                        operations.add(operation)
//                    } // TODO: decide change trust
////                    "change_trust" -> {
////                        op as ChangeTrustOperation
////                        operation.destinationAccount = op.
////                        operation.amount = op.limit
////                        operations.add(operation)
////                    }
//                    else -> {}
//                }
//            }
//
//            operationDao.insertOperations(*operations.toTypedArray())
//        }
//    }

    suspend fun syncOperations(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)

        withContext(Dispatchers.IO) {
            val operations = mutableListOf<Operation>()
            val operationResponses = mutableListOf<OperationResponse>()

            _error.postValue(stellarService.getOperations(accountId, operationResponses))

            operationResponses.forEach { op ->
                val operation = Operation(
                    operationId = op.id,
                    walletId = walletId,
                    transactionId = op.transactionHash,
                    operationType = op.type,
                    createdAt = op.createdAt,
                )

                when (operation.operationType) {
                    "payment" -> {
                        op as PaymentOperationResponse

                        operation.sourceAccount = op.from
                        operation.destinationAccount = op.to
                        operation.assetName = op.asset.type
                        operation.amount = op.amount

                        operation.isReceived = accountId == op.to

                        operations.add(operation)
                    }
                    "create_account" -> {
                        op as CreateAccountOperationResponse

                        operation.destinationAccount = op.account
                        operation.sourceAccount = op.funder
                        operation.amount = op.startingBalance

                        operations.add(operation)
                    } // TODO: decide change trust
//                    "change_trust" -> {
//                        op as ChangeTrustOperation
//                        operation.destinationAccount = op.
//                        operation.amount = op.limit
//                        operations.add(operation)
//                    }
                    else -> {}
                }
            }

            operationDao.insertOperations(*operations.toTypedArray())
        }
    }

    suspend fun syncTransactionsFromNetwork(walletId: Long) {
        val accountId = getAccountIdFromWalletId(walletId)

        withContext(Dispatchers.IO) {
            val transactionResponses = mutableListOf<TransactionResponse>()
            _error.postValue(stellarService.getTransactions(accountId, transactionResponses))
            val transactions: ArrayList<Transaction> = ArrayList()

            transactionResponses.forEach { tr ->
                val t = Transaction(
                    tr.hash, tr.isSuccessful, tr.sourceAccount, tr.operationCount, tr.createdAt
                )

                transactions.add(t)
                syncOperations(walletId)
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

    suspend fun generateNewWallet(walletName: String, secretPhrase: String): HashMap<String,String> {
        return withContext(Dispatchers.IO) {
            val ret = stellarService.generateAccount()
            val error = ret.first
            if (error != Error.NO_ERROR) {
                _error.postValue(error)
                return@withContext HashMap()
            }
            val generatedKeyPair = ret.second
            val wallet = Wallet()
            wallet.name = walletName
            wallet.privateKey = Encryption
                .encrypt(String(generatedKeyPair.secretSeed), secretPhrase)
                .toString()
            wallet.publicKey = generatedKeyPair.accountId
            insertWallet(wallet)
            val newKeypair = HashMap<String, String>()
            newKeypair["public_key"] = wallet.publicKey
            newKeypair["private_key"] = String(generatedKeyPair.secretSeed)
            return@withContext newKeypair
        }
    }

    suspend fun addExistingWallet(walletName: String, walletSecretSeed: String, secretPhrase: String): Long {
        return withContext(Dispatchers.IO) {

            val wallet = Wallet()
            wallet.name = walletName
            try {
                wallet.publicKey = pairSecretSeedWithPublicKey(walletSecretSeed)
            } catch (e:Exception) {
                _error.postValue(Error.ERROR_STELLAR)
                return@withContext -1L
            }
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
