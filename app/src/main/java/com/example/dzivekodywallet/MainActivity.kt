package com.example.dzivekodywallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.dzivekodywallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val binding = DataBindingUtil
//            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)

//        drawerLayout = binding.drawerLayout

//        val navController = this.findNavController(R.id.main_fragment_container)

//        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)

//        NavigationUI.setupWithNavController(binding.mainFragmentContainer, navController)

//        if (savedInstanceState == null) {
//            val fragment = AuthFragment()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.main_activity, fragment)
//                .commit()
//        }


//        binding.walletBottomNav.setOnItemSelectedListener {
    }
}