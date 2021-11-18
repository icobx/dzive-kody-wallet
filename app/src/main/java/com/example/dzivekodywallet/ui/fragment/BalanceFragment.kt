package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

//        requireActivity().onBackPressedDispatcher.addCallback(
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d("in callback", "in callback")
//                    Navigation.findNavController(binding.root)
//                        .popBackStack(R.id.wallet_man_activity, false)
//                }
//            }
//        )

        viewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]

        binding.balancesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.balances.observe(viewLifecycleOwner, { walletBalances ->
            binding.balancesRecyclerView.adapter = BalancesAdapter(walletBalances)
        })

        viewModel.updateBalance()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateBalance()
    }
}
