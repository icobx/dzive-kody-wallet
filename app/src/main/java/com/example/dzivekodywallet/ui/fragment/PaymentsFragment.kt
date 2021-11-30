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
import com.example.dzivekodywallet.databinding.FragmentPaymentsBinding
import com.example.dzivekodywallet.databinding.FragmentTransactionsBinding
import com.example.dzivekodywallet.ui.adapter.PaymentsAdapter
import com.example.dzivekodywallet.ui.adapter.TransactionItemListener
import com.example.dzivekodywallet.ui.adapter.TransactionsAdapter
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class PaymentsFragment : Fragment() {
    private lateinit var binding: FragmentPaymentsBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletViewModelFactory(requireContext())
        )[WalletViewModel::class.java]

        binding.paymentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.paymentsRecyclerView.adapter = PaymentsAdapter()

        viewModel.updatePayments()

        viewModel.payments.observe(viewLifecycleOwner, { payments ->
            if (payments != null) {
                (binding.paymentsRecyclerView.adapter as PaymentsAdapter)
                    .setPayments(payments)
            }
        })

        return binding.root
    }

}
