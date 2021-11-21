package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentTransactionDetailsBinding
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class TransactionDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletViewModelFactory(requireContext())
        )[WalletViewModel::class.java]

        val args = TransactionDetailsFragmentArgs.fromBundle(requireArguments())
        Log.d(javaClass.name, "Obtained trId: ${args.transactionId}")

        return binding.root
    }
}