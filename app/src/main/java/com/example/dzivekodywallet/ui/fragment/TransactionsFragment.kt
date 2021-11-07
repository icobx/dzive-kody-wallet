package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dzivekodywallet.databinding.FragmentTransactionsBinding

class TransactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionsBinding.inflate(
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreated", "in transactions fragment")
    }

}