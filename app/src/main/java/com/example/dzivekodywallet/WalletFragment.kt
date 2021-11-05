package com.example.dzivekodywallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dzivekodywallet.databinding.FragmentWalletBinding
import com.google.android.material.navigation.NavigationBarView

class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        binding.walletBottomNav.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.wallet_menu_balance -> {
//
////                    setContent("Home")
//                    true
//                }
//                R.id.wallet_menu_transactions -> {
////                    setContent("Notification")
//                    true
//                }
//                R.id.wallet_menu_sendrec -> {
////                    setContent("Search")
//                    true
//                }
//                else -> false
//            }
//        }

    }



}