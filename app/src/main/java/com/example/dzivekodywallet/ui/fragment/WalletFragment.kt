package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentWalletBinding
import com.example.dzivekodywallet.viewmodel.WalletViewModel

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(
            inflater,
            container,
            false
        )
        // back button nav
        // TODO: no anim on button press atm
        binding.walletAppbar.setNavigationOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_walletFragment_to_walletsFragment)
        }

        // bottom nav functionality
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.wallet_fragment_container) as NavHostFragment
        binding.walletBottomNav.setupWithNavController(navHostFragment.findNavController())

        val args = WalletFragmentArgs.fromBundle(requireArguments())

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletViewModelFactory(requireContext())
        )[WalletViewModel::class.java]

        viewModel.setWalletId(args.walletId)

        viewModel.updateBalance()

        return binding.root
    }
}