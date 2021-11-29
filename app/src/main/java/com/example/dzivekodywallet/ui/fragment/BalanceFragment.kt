package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.databinding.FragmentBalanceBinding
import com.example.dzivekodywallet.ui.adapter.BalancesAdapter
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class BalanceFragment : Fragment() {
    private lateinit var binding: FragmentBalanceBinding

    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBalanceBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]

        binding.balancesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.balancesRecyclerView.adapter = BalancesAdapter()

        viewModel.updateBalance()

        viewModel.balances.observe(viewLifecycleOwner, { walletBalances ->
            if (walletBalances != null) {
                (binding.balancesRecyclerView.adapter as BalancesAdapter)
                    .setBalances(walletBalances)
            }
        })

        return binding.root
    }
}
