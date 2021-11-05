package com.example.dzivekodywallet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDatabaseDao {
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

    @Insert
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

    @Query("SELECT * from wallet_table WHERE walletId = :key")
    fun getWallet(key: Long): Wallet?

    @Query("SELECT * FROM wallet_table ORDER BY walletId DESC")
    fun getAllWallets(): LiveData<List<Wallet>>
}
