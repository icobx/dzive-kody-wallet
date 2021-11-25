package com.example.dzivekodywallet.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.util.CopyOnClickListener
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentTransactionDetailsBinding
import com.example.dzivekodywallet.ui.adapter.TransactionDetailsAdapter
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class TransactionDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var viewModel: WalletViewModel

    @SuppressLint("ServiceCast")
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

        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)

        binding.transactionDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionDetailsRecyclerView.adapter = TransactionDetailsAdapter(clipboard!!)

        binding.transactionDetailsBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_transactionDetailsFragment_to_wallet_nav_transactions)
        }

        viewModel.selectedTransaction.observe(viewLifecycleOwner, { selectedTransaction ->
            binding.transaction = selectedTransaction

            binding.transactionDetailsTransIdCopyButton.setOnClickListener(CopyOnClickListener(
                clipboard,
                "Transaction ID",
                selectedTransaction.transactionId
            ))

            binding.transactionDetailsTransSrcAccCopyButton.setOnClickListener(CopyOnClickListener(
                clipboard,
                "Transaction Source Account ID",
                selectedTransaction.sourceAccountId
            ))
        })

        viewModel.operations.observe(viewLifecycleOwner, { ops ->
            ops?.let {
                (binding.transactionDetailsRecyclerView.adapter as TransactionDetailsAdapter)
                    .setOperations(ops)
            }
        })

        return binding.root
    }
}
