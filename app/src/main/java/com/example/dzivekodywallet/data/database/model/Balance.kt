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

    @ColumnInfo(name = "walletId")
    var walletId: Long? = null,


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Balance

        if (balanceId != other.balanceId) return false
        if (assetName != other.assetName) return false
        if (amount != other.amount) return false
        if (walletId != other.walletId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = balanceId?.hashCode() ?: 0
        result = 31 * result + assetName.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + (walletId?.hashCode() ?: 0)
        return result
    }
}
