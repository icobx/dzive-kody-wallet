package com.example.dzivekodywallet.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operation_table")
data class Operation (
    @PrimaryKey(autoGenerate = true)
    var operationId: Long,

    @ColumnInfo(name = "operation_type")
    var operationType: String = "",

    @ColumnInfo(name = "destination_account")
    var destinationAccount: String = "",

    @ColumnInfo(name = "amount")
    var amount: String = "",

    @ColumnInfo(name = "assetName")
    var assetName: String = "",

    @ColumnInfo(name = "transaction_id")
    var transactionId: String,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Operation

        if (operationId != other.operationId) return false
        if (operationType != other.operationType) return false
        if (destinationAccount != other.destinationAccount) return false
        if (amount != other.amount) return false
        if (assetName != other.assetName) return false
        if (transactionId != other.transactionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = operationId.hashCode()
        result = 31 * result + operationType.hashCode()
        result = 31 * result + destinationAccount.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + assetName.hashCode()
        result = 31 * result + transactionId.hashCode()
        return result
    }
}
