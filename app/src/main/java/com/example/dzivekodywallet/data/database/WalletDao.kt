package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dzivekodywallet.data.database.model.Wallet

@Dao
interface WalletDao {
    @Insert
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

    @Query("SELECT * from wallet_table WHERE walletId = :key")
    fun getWallet(key: Long): Wallet?

    @Query("SELECT * FROM wallet_table ORDER BY walletId DESC")
    fun getAllWallets(): LiveData<List<Wallet>>
}
