package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentAddWalletBinding
import com.example.dzivekodywallet.viewmodel.AddWalletViewModel
import com.example.dzivekodywallet.viewmodel.BalanceViewModel

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

        binding.buttonAddExistingWallet.setOnClickListener {
            addExistingWallet()
        }
        return binding.root
    }

    private fun addExistingWallet() {
        viewModel.addExistingWallet(binding.walletName, binding.walletAccountId, binding.walletSecretSeed)

        // TODO
        // check if addition succeeded
        findNavController().navigate(AddWalletFragmentDirections.actionAddWalletFragment2ToWalletsFragment())
    }
}