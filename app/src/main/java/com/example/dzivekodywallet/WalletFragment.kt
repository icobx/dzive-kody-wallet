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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.wallet_fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.walletBottomNav.setupWithNavController(navController)
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