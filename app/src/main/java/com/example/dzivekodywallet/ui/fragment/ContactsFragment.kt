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
import androidx.navigation.fragment.findNavController
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
                { contactId ->
                    findNavController().navigate(WalletsFragmentDirections.actionWalletsFragmentToWalletFragment(contactId))
                },
                { contact ->
                    viewModel.deleteContact(contact)
                })
            )
        })

//        binding.chooseWalletButton.setOnClickListener { view: View ->
//            view.findNavController().navigate(R.id.action_walletsFragment_to_walletFragment)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated", "in wallets fragment")
//        val nc = findNavController()
//        val nc = activity?.findNavController(R.id.main_fragment_container)
//        val button = binding.chooseWalletButton
//
//        button.setOnClickListener {
//            nc?.navigate(R.id.action_walletsFragment_to_wallet_nav_graph)
//        }

        val button = binding.addContactImageButton

        var counter = 2

        button.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact,null)
            dialog.setContentView(dialogView)

            // Buttons
            val closeButton = dialogView.findViewById<ImageView>(R.id.dialog_add_contact_close)
            val name = dialogView.findViewById<EditText>(R.id.dialog_add_contact_name)
            val pubKey = dialogView.findViewById<EditText>(R.id.dialog_add_contact_pubkey)

            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.dialog_add_contact_save_button).setOnClickListener {
                val contact = Contact()
                contact.name = name.text.toString()
                contact.pubKey = pubKey.text.toString()
                viewModel.insertContact(contact)
                dialog.dismiss()
            }

            dialog.show()


//            contact.name = "David Spak"
//            contact.pubKey = "disahfdsjkfhdslkrhoiwjhro32nrkj32rnfewnklfnmdslkfdslf"
//            viewModel.insertContact(contact)

//            val wallet = Wallet()
//            wallet.name = "wallet ${counter++}"
//            wallet.balance = 121.5
//            viewModel.insertWallet(wallet)
//            findNavController().navigate(R.id.action_walletsFragment_to_walletFragment)
        }
    }
}