package com.example.dzivekodywallet.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.database.ContactDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepository private constructor(private val contactDao: ContactDao) {

    fun insertContact(wallet: Contact) {
        contactDao.insertContact(wallet)
    }

    fun updateContact(wallet: Contact) {
        contactDao.updateContact(wallet)
    }

    fun deleteContact(wallet: Contact) {
        contactDao.deleteContact(wallet)
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return contactDao.getAllContacts()
    }

    companion object {
        @Volatile
        private var INSTANCE: ContactRepository? = null


        fun getInstance(walletDao: ContactDao): ContactRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = ContactRepository(walletDao)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}