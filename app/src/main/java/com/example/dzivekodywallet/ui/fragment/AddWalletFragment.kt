package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentAddWalletBinding
import com.example.dzivekodywallet.viewmodel.AddWalletViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: pri generovani novej wallet umoznit pouzivatelovi pozriet si public a secret key
class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var viewModel: AddWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWalletBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideAddWalletViewModelFactory(requireContext())
        )[AddWalletViewModel::class.java]

        binding.buttonAddWallet.setOnClickListener {
            addWallet()
        }

        binding.pBar.visibility = View.GONE
        binding.isGeneratingEnabled = binding.checkBoxGenerateNew.isChecked

        binding.checkBoxGenerateNew.setOnClickListener { view ->
            val checkBox = (view as CheckBox)
            binding.isGeneratingEnabled = checkBox.isChecked
        }

        return binding.root
    }

    private fun addWallet() {
        // TODO: secretPhrase zmenit na ziskavanie PIN kodu z UI
        if (binding.walletName != null || binding.walletSecretSeed != null) {
            binding.textInputWalletName.isEnabled = false
            binding.textInputSecretSeed.isEnabled = false
            binding.buttonAddWallet.setOnClickListener(null)
            binding.pBar.visibility = View.VISIBLE
            viewModel.addWallet(binding.walletName.toString(), binding.walletSecretSeed.toString(), "1234", binding.isGeneratingEnabled as Boolean, this::openModal)
            if (binding.isGeneratingEnabled == false) {
                findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
            }
        }
        // TODO: check if addition succeeded
    }


    fun openModal(publicKey: String, privateKey: String) {
        binding.pBar.visibility = View.GONE
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_wallet_information,null)
        dialog.setContentView(dialogView)

        // Buttons
        val closeButton = dialogView.findViewById<ImageView>(R.id.dialog_wallet_information_close)
        val privKey = dialogView.findViewById<TextView>(R.id.dialog_wallet_information_private_key)
        val pubKey = dialogView.findViewById<TextView>(R.id.dialog_wallet_information_pub_key)

        privKey.setText(privateKey)
        pubKey.setText(publicKey)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.dialog_wallet_information_confirm_button).setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
        }

        dialog.show()
    }


}