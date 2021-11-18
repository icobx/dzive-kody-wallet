package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.viewmodel.WalletsViewModel
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentWalletsBinding
import com.example.dzivekodywallet.ui.adapter.WalletItemAdapter
import com.example.dzivekodywallet.ui.adapter.WalletItemListener

class WalletsFragment : Fragment() {
    private lateinit var binding: FragmentWalletsBinding
    private lateinit var viewModel: WalletsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletsViewModelFactory(requireContext())
        )[WalletsViewModel::class.java]

        binding.walletsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getWallets().observe(viewLifecycleOwner, Observer { wallets ->
            binding.walletsRecyclerView.adapter = WalletItemAdapter(wallets, WalletItemListener(
                { walletId ->
                    findNavController().navigate(WalletsFragmentDirections.actionWalletsFragmentToWalletFragment(walletId))
                },
                { wallet ->
                    viewModel.deleteWallet(wallet)
                })
            )
        })

        return binding.root
    }

    private fun addExistingWallet() {
        findNavController().navigate(WalletsFragmentDirections.actionWalletsFragmentToAddWalletFragment2())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addExistingButton =binding.addWalletImageButton

        addExistingButton.setOnClickListener {
            addExistingWallet()
        }
    }
}