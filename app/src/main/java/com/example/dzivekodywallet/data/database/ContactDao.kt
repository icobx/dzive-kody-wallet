package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContact(contact: Contact)

    @Update
    fun updateContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)

    @Query("SELECT * from contacts_table WHERE contactId = :key")
    fun getContact(key: Long): Contact?

    @Query("SELECT * FROM contacts_table ORDER BY contactId DESC")
    fun getAllContacts(): LiveData<List<Contact>>
}
