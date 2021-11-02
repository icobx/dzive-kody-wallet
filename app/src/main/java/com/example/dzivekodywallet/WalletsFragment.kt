package com.example.dzivekodywallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        return binding.root
    }
}