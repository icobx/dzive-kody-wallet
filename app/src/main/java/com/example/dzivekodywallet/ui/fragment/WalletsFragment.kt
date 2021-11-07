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
import com.example.dzivekodywallet.viewmodel.WalletItemViewModel
import com.example.dzivekodywallet.viewmodel.WalletsViewModel
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentWalletsBinding

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
            requireActivity(),//this,
            Injection.provideWalletsViewModelFactory(requireContext())
        ).get(WalletsViewModel::class.java)


        viewModel.getWallets().observe(viewLifecycleOwner, Observer { wallets ->
            val stringBuilder = StringBuilder()
            wallets.forEach { wallet ->
                stringBuilder.append("${wallet.name}\n\n")
            }
            binding.textViewWallets.text = stringBuilder.toString()
//            textView_wallets.text = stringBuilder.toString()
        })

        binding.walletsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<WalletItemViewModel>()

//        for (i in 1..20) {
//            data.add(WalletItemViewModel("Wallet " + i))
//        }

//        data.add(WalletItemViewModel("Wallet 1"))
//        viewModel.getWallets().observe(viewLifecycleOwner, Observer {
//                wallets ->
//            wallets.forEach { wallet -> {}
//                data.add(WalletItemViewModel(wallet.name))
//            }
//        })

//        val adapter = WalletItemAdapter(data)
//        binding.walletsRecyclerView.adapter = adapter


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
        var counter = 1
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