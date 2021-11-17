package com.example.dzivekodywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.ContactRepository

class SendReceiveViewModelFactory(private val contactRepository: ContactRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SendReceiveViewModel(contactRepository) as T
//        return super.create(modelClass)
    }
}