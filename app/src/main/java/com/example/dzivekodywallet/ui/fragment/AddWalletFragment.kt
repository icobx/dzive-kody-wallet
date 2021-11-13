package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentAddWalletBinding
import com.example.dzivekodywallet.viewmodel.AddWalletViewModel

class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var viewModel: AddWalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        binding.isGeneratingEnabled = binding.checkBoxGenerateNew.isChecked

        binding.checkBoxGenerateNew.setOnClickListener { view ->
            val checkBox = (view as CheckBox)
            binding.isGeneratingEnabled = checkBox.isChecked
        }

        return binding.root
    }

    private fun addWallet() {
        viewModel.addWallet(binding.walletName.toString(), binding.walletSecretSeed.toString(), "1234", binding.isGeneratingEnabled as Boolean)
        // TODO
        // check if addition succeeded
        findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
    }
}