package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.data.util.Error
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentAddWalletBinding
import com.example.dzivekodywallet.viewmodel.AddWalletViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        viewModel.error.observe(viewLifecycleOwner, { error ->
            if (error != Error.NO_ERROR) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = error.toString()
                binding.pBar.visibility = View.GONE
                enableForm()
            }
        })

        return binding.root
    }

    private fun addWallet() {
        if (binding.walletName != null || binding.walletSecretSeed != null) {
            disableForm()
            binding.pBar.visibility = View.VISIBLE
            viewModel.addWallet(
                binding.walletName.toString(),
                binding.walletSecretSeed.toString(),
                binding.walletPin.toString(),
                binding.isGeneratingEnabled as Boolean,
                this::openModal, this::callback)
        }
    }

    private fun callback() {
        binding.errorText.visibility = View.GONE
        findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
    }

    private fun disableForm() {
        binding.errorText.visibility = View.GONE
        binding.textInputWalletName.isEnabled = false
        binding.textInputSecretSeed.isEnabled = false
        binding.checkBoxGenerateNew.isEnabled = false

        binding.buttonAddWallet.isClickable = false
        binding.buttonAddWallet.isEnabled = false
    }

    private fun enableForm() {
        binding.textInputWalletName.isEnabled = true
        binding.textInputSecretSeed.isEnabled = true
        binding.checkBoxGenerateNew.isEnabled = true

        binding.buttonAddWallet.isClickable = true
        binding.buttonAddWallet.isEnabled = true
    }


    fun openModal(publicKey: String, privateKey: String) {
        binding.pBar.visibility = View.GONE
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_wallet_information,null)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val privKey = dialogView.findViewById<TextView>(R.id.dialog_wallet_information_private_key)
        val pubKey = dialogView.findViewById<TextView>(R.id.dialog_wallet_information_pub_key)

        privKey.text = privateKey
        pubKey.text = publicKey

        dialogView.findViewById<Button>(R.id.dialog_wallet_information_confirm_button).setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
        }

        dialog.show()
    }
}