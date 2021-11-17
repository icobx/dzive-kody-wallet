package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.databinding.FragmentBalanceBinding
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

        viewModel.balance.observe(viewLifecycleOwner, Observer { balance ->
            binding.balanceTextView.text = balance.toString()
        })

        viewModel.updateBalance()

        return binding.root
    }
}