package com.example.dzivekodywallet.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "balances_table",
    foreignKeys = [ForeignKey(
        entity = Wallet::class,
        parentColumns = arrayOf("walletId"),
        childColumns = arrayOf("walletId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Balance (
    @PrimaryKey(autoGenerate = true)
    var balanceId: Long? = null,

    @ColumnInfo(name = "assetName")
    var assetName: String = "",

    @ColumnInfo(name = "amount")
    var amount: Double = 0.0,

    @ColumnInfo(name = "wallet")
    var wallet: Long? = null,

)
