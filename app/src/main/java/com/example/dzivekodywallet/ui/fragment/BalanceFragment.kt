package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dzivekodywallet.data.WalletRepository
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentBalanceBinding
import com.example.dzivekodywallet.viewmodel.BalanceViewModel
import com.example.dzivekodywallet.viewmodel.BalanceViewModelFactory
import com.example.dzivekodywallet.viewmodel.WalletsViewModel

class BalanceFragment : Fragment() {
    private lateinit var binding: FragmentBalanceBinding

    private lateinit var viewModel: BalanceViewModel
    private lateinit var viewModelFactory: BalanceViewModelFactory

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

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideBalanceViewModelFactory(requireContext())
        )[BalanceViewModel::class.java]

//        requireActivity().onBackPressedDispatcher.addCallback(
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d("in callback", "in callback")
//                    Navigation.findNavController(binding.root)
//                        .popBackStack(R.id.wallet_man_activity, false)
//                }
//            }
//        )


        viewModel.balance.observe(viewLifecycleOwner, Observer { balance ->
            binding.balanceTextView.text = balance.toString()
        })

        viewModel.updateBalance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreated", "in balance fragment")
    }

}