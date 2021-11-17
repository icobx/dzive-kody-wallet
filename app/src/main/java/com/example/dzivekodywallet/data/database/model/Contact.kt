package com.example.dzivekodywallet.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts_table")
data class Contact (
    @PrimaryKey(autoGenerate = true)
    var contactId: Long? = null,

    @ColumnInfo(name = "pub_key")
    var pubKey: String = "",

    @ColumnInfo(name = "name")
    var name: String = ""
)

