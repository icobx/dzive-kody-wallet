package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzivekodywallet.data.ContactRepository
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.util.Error
import kotlinx.coroutines.*

data class ContactsViewModel(private val contactRepository: ContactRepository): ViewModel() {

    private var _allContacts = MutableLiveData<List<Contact>>()
    val allContacts: LiveData<List<Contact>>
        get() = _allContacts

    fun getContacts(): LiveData<List<Contact>> {
        return contactRepository.getAllContacts()
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.insertContact(contact)
        }
    }

    fun editContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.updateContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.deleteContact(contact)
        }
    }
}
