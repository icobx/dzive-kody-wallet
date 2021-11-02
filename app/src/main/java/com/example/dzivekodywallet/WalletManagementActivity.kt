package com.example.dzivekodywallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WalletManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_management)

        if (savedInstanceState == null) {
            val fragment = WalletsFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.wallet_man_activity, fragment)
                .commit()
        }
    }
}