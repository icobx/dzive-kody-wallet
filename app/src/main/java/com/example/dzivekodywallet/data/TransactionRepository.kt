package com.example.dzivekodywallet.data

import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.database.TransactionDao

class TransactionRepository private constructor(private val transactionDao: TransactionDao) {

    fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    fun getTransaction(key: Long): Transaction? {
        return transactionDao.getTransaction(key)
    }

//    fun getAllTransactions(): LiveData<List<Transaction>> {
//        return transactionDao.getAllTransactions()
//    }

    fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null


        fun getInstance(transactionDao: TransactionDao): TransactionRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = TransactionRepository(transactionDao)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}