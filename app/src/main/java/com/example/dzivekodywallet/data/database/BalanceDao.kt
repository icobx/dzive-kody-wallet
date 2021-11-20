package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Balance

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBalance(Balance: Balance): Long

    @Update
    fun updateBalance(Balance: Balance)

    @Delete
    fun deleteBalance(Balance: Balance)

    @Query("SELECT * from balances_table WHERE balanceId = :balanceId")
    fun findBalanceById(balanceId: Long): Balance?

    @Query("SELECT * from balances_table WHERE walletId = :walletId and assetName = :assetName")
    fun findAssetForWallet(walletId: Long, assetName: String): Balance?

    @Query("SELECT EXISTS(SELECT 1 FROM balances_table WHERE walletId = :walletId and assetName = :assetName)")
    fun assetExists(walletId: Long, assetName: String): Boolean

    @Query("SELECT * from balances_table WHERE walletId = :walletId")
    fun getBalances(walletId: Long): LiveData<List<Balance>>

    @Query("SELECT * from balances_table")
    fun getBalances(): LiveData<List<Balance>>

}
