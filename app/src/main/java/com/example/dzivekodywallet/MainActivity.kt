package com.example.dzivekodywallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.stellar.sdk.KeyPair

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create a completely new and unique pair of keys.
//         see more about KeyPair objects: https://stellar.github.io/java-stellar-sdk/org/stellar/sdk/KeyPair.html
//        KeyPair pair = KeyPair.random();

        var pair: KeyPair = KeyPair.random()

//        println(pair.secretSeed)
        Log.d("patres ma maly khok", String(pair.secretSeed))

//        System.out.println(new String(pair.getSecretSeed()));
        // SAV76USXIJOBMEQXPANUOQM6F5LIOTLPDIDVRJBFFE2MDJXG24TAPUU7
//        System.out.println(pair.getAccountId());
//         GCFXHS4GXL6BVUCXBWXGTITROWLVYXQKQLF4YH5O5JT3YZXCYPAFBJZB
    }
}