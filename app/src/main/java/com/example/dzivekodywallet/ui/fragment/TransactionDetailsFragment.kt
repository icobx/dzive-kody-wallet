package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentTransactionDetailsBinding
import com.example.dzivekodywallet.ui.adapter.TransactionDetailsAdapter
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class TransactionDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var viewModel: WalletViewModel

    private lateinit var transaction: Transaction

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

        transaction = Transaction(args.transactionId)
        transaction.sourceAccountId = args.sourceAccountId

        binding.transaction = transaction
        viewModel.setSelectedTransaction(transaction)

        binding.transactionDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionDetailsRecyclerView.adapter = TransactionDetailsAdapter()

        viewModel.operations.observe(viewLifecycleOwner, { ops ->
            ops?.let {
                (binding.transactionDetailsRecyclerView.adapter as TransactionDetailsAdapter)
                    .setOperations(ops)
            }
        })

        return binding.root
    }
}
