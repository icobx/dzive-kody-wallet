package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentContactsBinding
import com.example.dzivekodywallet.ui.adapter.ContactItemAdapter
import com.example.dzivekodywallet.ui.adapter.ContactItemListener
import com.example.dzivekodywallet.viewmodel.ContactsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var viewModel: ContactsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideContactsViewModelFactory(requireContext())
        )[ContactsViewModel::class.java]



        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getContacts().observe(viewLifecycleOwner, Observer { contacts ->
            binding.contactsRecyclerView.adapter = ContactItemAdapter(contacts, ContactItemListener(
                { contact ->
                  openModal(contact, this::editContact)
                },
                { contact ->
                    viewModel.deleteContact(contact)
                })
            )
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.addContactImageButton

        button.setOnClickListener {
            openModal(null, this::createContact)
        }
    }

    fun createContact(contact: Contact) {
        viewModel.insertContact(contact)
    }

    fun editContact(contact: Contact) {
        viewModel.editContact(contact)
    }

    fun openModal(_contact: Contact?, handler: (contact: Contact) -> Unit) {
        var contact: Contact? = null
        if(_contact !== null) {
            contact = _contact
        } else {
            contact = Contact()
        }

        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact,null)
        dialog.setContentView(dialogView)

        // Buttons
        val closeButton = dialogView.findViewById<ImageView>(R.id.dialog_add_contact_close)
        val name = dialogView.findViewById<EditText>(R.id.dialog_add_contact_name)
        val pubKey = dialogView.findViewById<EditText>(R.id.dialog_add_contact_pubkey)

        name.setText(contact.name)
        pubKey.setText(contact.pubKey)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.dialog_add_contact_save_button).setOnClickListener {
//            val contact = Contact()
            contact.name = name.text.toString()
            contact.pubKey = pubKey.text.toString()

            handler(contact)

            dialog.dismiss()
        }

        dialog.show()
    }
}