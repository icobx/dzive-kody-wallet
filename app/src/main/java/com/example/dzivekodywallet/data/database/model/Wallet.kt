package com.example.dzivekodywallet.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_table")
data class Wallet (
    @PrimaryKey(autoGenerate = true)
    var walletId: Long? = null,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "balance")
    var balance: Double = 0.0,

    @ColumnInfo(name = "public_key")
    var publicKey: String = "",

    @ColumnInfo(name = "private_key")
    var privateKey: String = "",

)

