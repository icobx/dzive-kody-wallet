package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.data.database.model.Wallet

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

    @Delete
    fun deleteWallet(wallet: Wallet)

    @Query("SELECT * from wallet_table WHERE walletId = :key")
    fun getWallet(key: Long): Wallet?

    @Query("SELECT * FROM wallet_table ORDER BY walletId DESC")
    fun getAllWallets(): LiveData<List<Wallet>>

    @Query("SELECT * from balances_table WHERE wallet = :walletId")
    fun getBalancesForWallet(walletId: Long): List<Balance>

}
