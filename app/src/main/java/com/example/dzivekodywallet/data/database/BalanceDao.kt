package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Balance

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBalance(Balance: Balance)

    @Update
    fun updateBalance(Balance: Balance)

    @Delete
    fun deleteBalance(Balance: Balance)
}
