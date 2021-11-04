package com.example.dzivekodywallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.databinding.FragmentWalletsBinding

class WalletsFragment : Fragment() {
    private lateinit var binding: FragmentWalletsBinding

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

        binding.chooseWalletButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_walletsFragment_to_walletFragment)
        }

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
    }
}