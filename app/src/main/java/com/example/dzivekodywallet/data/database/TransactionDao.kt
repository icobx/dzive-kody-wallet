package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransactions(vararg transaction: Transaction)

    @Update
    fun updateTransaction(transaction: Transaction)

    @Query("SELECT * from transaction_table WHERE transactionId = :key")
    fun getTransaction(key: Long): Transaction?

    @Query("SELECT * FROM transaction_table WHERE source_account_id = :sourceAccId ORDER BY created_at DESC")
    fun getTransactions(sourceAccId: String): LiveData<List<Transaction>>

    @Query("DELETE FROM transaction_table")
    fun deleteAllTransactions()
}
