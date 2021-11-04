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
//        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
//            R.layout.fragment_title,container,false)

        binding.correctPinButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_authFragment_to_walletsFragment)
        }
        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textView = "tu sa bude davat pin"

//        val navController = activity?.findNavController(R.id.main_fragment_container)
//        val button = binding.correctPinButton
//
//        button.setOnClickListener {
//            navController?.navigate(R.id.action_authFragment_to_walletsFragment)
////            navController?.popBackStack(R.layout.)
//        }

        Log.d("onViewCreated", "ANO")
        // ...and so on
    }

}
