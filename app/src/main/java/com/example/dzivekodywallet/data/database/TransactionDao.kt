package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dzivekodywallet.data.database.model.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transaction: Transaction)

    // TODO: will we need modify transactions?
    @Update
    fun updateTransaction(transaction: Transaction)

    @Query("SELECT * from transaction_table WHERE transactionId = :key")
    fun getTransaction(key: Long): Transaction?

    @Query("SELECT * FROM transaction_table ORDER BY transactionId DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("DELETE FROM transaction_table")
    fun deleteAllTransactions()
}
