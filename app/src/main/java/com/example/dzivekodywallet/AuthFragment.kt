package com.example.dzivekodywallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    // Leave empty for now.
    // View initialization logic
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textView = "data premenna"

//        val navHostFragment =
//            view.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = findNavController()
        val button = view.findViewById<Button>(R.id.correct_pin_button)

        button.setOnClickListener {
            navController.navigate(R.id.action_auth_fragment_to_wallet_man_nav_graph)
        }
        // Connect adapters
//        productAdapter = ProductAdapter(productClickCallback)
//        binding.productsList.setAdapter(productAdapter)
//
//        // Initialize view properties, set click listeners, etc.
//        binding.productsSearchBtn.setOnClickListener {...}
//
//        // Subscribe to state
//        viewModel.products.observe(this, Observer { myProducts ->
//            ...
//        })
        Log.d("onViewCreated", "ANO")
        // ...and so on
    }

}
