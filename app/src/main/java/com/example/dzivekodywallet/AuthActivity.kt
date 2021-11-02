package com.example.dzivekodywallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            val fragment = AuthFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.auth_activity, fragment)
                .commit()
        }
    }
}