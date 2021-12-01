package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.util.Error
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

        val args = WalletFragmentArgs.fromBundle(requireArguments())

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletViewModelFactory(requireContext())
        )[WalletViewModel::class.java]

        viewModel.setWalletId(args.walletId)
        viewModel.walletName.observe(viewLifecycleOwner, { name ->
            binding.walletAppbar.title = name
        })

        viewModel.updateName()

        binding.walletAppbar.setNavigationOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_walletFragment_to_walletsFragment)
        }
        binding.walletAppbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.wallet_appbar_sync -> {
                    viewModel.synchronise()
                    true
                }
                else -> false
            }
        }

        // Display error in ErrorBar, otherwise reset to defaults
        viewModel.error.observe(viewLifecycleOwner, { error ->
            if (error != Error.NO_ERROR) {
                binding.walletErrorBar.visibility = View.VISIBLE
                binding.walletErrorBar.text = error.toString()

                binding.walletErrorBar.postDelayed({
                    binding.walletErrorBar.visibility = View.GONE
                    binding.walletErrorBar.text = ""
                }, 2000)
            }
        })

        // bottom nav functionality
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.wallet_fragment_container) as NavHostFragment
        binding.walletBottomNav.setupWithNavController(navHostFragment.findNavController())

        return binding.root
    }
}
