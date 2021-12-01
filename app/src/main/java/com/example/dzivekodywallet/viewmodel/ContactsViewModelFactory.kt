package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.ContactRepository

class ContactsViewModelFactory(private val contactRepository: ContactRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsViewModel(contactRepository) as T
    }
}