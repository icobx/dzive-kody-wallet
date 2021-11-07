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
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.viewmodel.WalletsViewModel
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentWalletsBinding
import com.example.dzivekodywallet.ui.adapter.WalletItemAdapter

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
        ).get(WalletsViewModel::class.java)

        binding.walletsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getWallets().observe(viewLifecycleOwner, Observer {
                wallets -> binding.walletsRecyclerView.adapter = WalletItemAdapter(wallets)
        })

//        binding.chooseWalletButton.setOnClickListener { view: View ->
//            view.findNavController().navigate(R.id.action_walletsFragment_to_walletFragment)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated", "in wallets fragment")
//        val nc = findNavController()
//        val nc = activity?.findNavController(R.id.main_fragment_container)
//        val button = binding.chooseWalletButton
//
//        button.setOnClickListener {
//            nc?.navigate(R.id.action_walletsFragment_to_wallet_nav_graph)
//        }

        val button = binding.addWalletImageButton
        var counter = 2
        button.setOnClickListener {
            Log.d("onViewCreated", "add wallet click")
            val wallet = Wallet()
            wallet.name = "wallet ${counter++}"
            wallet.balance = 121.5
            viewModel.insertWallet(wallet)
            findNavController().navigate(R.id.action_walletsFragment_to_walletFragment)
        }
    }
}