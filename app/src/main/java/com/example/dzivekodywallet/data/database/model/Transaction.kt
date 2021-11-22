package com.example.dzivekodywallet.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "transaction_table")
data class Transaction (
    @PrimaryKey(autoGenerate = false)
    var transactionId: String,

    @ColumnInfo(name = "successful")
    var successful: Boolean,

    @ColumnInfo(name = "source_account_id")
    var sourceAccountId: String,

    @ColumnInfo(name = "operations_count")
    var operationsCount: Int,

    @ColumnInfo(name = "created_at")
    var createdAt: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (transactionId != other.transactionId) return false
        if (successful != other.successful) return false
        if (sourceAccountId != other.sourceAccountId) return false
        if (operationsCount != other.operationsCount) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transactionId.hashCode()
        result = 31 * result + successful.hashCode()
        result = 31 * result + sourceAccountId.hashCode()
        result = 31 * result + operationsCount
        result = 31 * result + createdAt.hashCode()
        return result
    }
}

