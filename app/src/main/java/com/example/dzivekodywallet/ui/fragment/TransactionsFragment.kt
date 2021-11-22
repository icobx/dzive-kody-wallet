package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentTransactionsBinding
import com.example.dzivekodywallet.ui.adapter.TransactionItemListener
import com.example.dzivekodywallet.ui.adapter.TransactionsAdapter
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class TransactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletViewModelFactory(requireContext())
        )[WalletViewModel::class.java]

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRecyclerView.adapter = TransactionsAdapter()

        viewModel.updateTransactions()

        viewModel.transactions.observe(viewLifecycleOwner, { transactions ->
            if (transactions != null) {
                (binding.transactionsRecyclerView.adapter as TransactionsAdapter)
                    .setTransactions(transactions)
                (binding.transactionsRecyclerView.adapter as TransactionsAdapter)
                    .setClickListener(
                        TransactionItemListener { transaction ->
                            findNavController()
                                .navigate(
                                    TransactionsFragmentDirections.actionWalletNavTransactionsToTransactionDetailsFragment(transaction.transactionId, transaction.sourceAccountId)
                        )}
                    )
            }
        })

        return binding.root
    }

}
