package com.example.dzivekodywallet.data
//
//import android.util.Log
////import com.github.kittinunf.fuel.Fuel
////import com.github.kittinunf.fuel.core.await
////import com.github.kittinunf.fuel.core.awaitResponse
////import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
//import kotlinx.coroutines.runBlocking
//import org.stellar.sdk.KeyPair
//import java.io.InputStream
//import java.net.URL
//import java.util.*
//import org.stellar.sdk.responses.AccountResponse
//
//import org.stellar.sdk.Server
//import kotlin.collections.ArrayList
//
//
//class Repository {
//
//
//
//    fun createAccount(): KeyPair {
//        var pair: KeyPair = KeyPair.random()
//
//        return pair
//    }
//
//    fun fund(pair: KeyPair) {
//        runBlocking {
//            val (request, response, result) = Fuel.get(
//                "https://friendbot.stellar.org/?addr=${pair.accountId}"
//            ).awaitStringResponseResult()
//
//        }
//
//
//    }
//
//    fun accInfo(pair: KeyPair) {
//        runBlocking {
//            var pubK = "GDMUYFD7G3TUG7MJD5NZQ2Q7J3K6XYEDUEUYYENHHOMUZSEVMPQLF3NP"
//            var url = "https://horizon-testnet.stellar.org/accounts/${pubK}"
//            val (request, response, result) = Fuel.get(url).awaitStringResponseResult()
//
//            Log.d("TEST REQUEST RESPONSE", "$request")
//            Log.d("TEST REQUEST RESPONSE", "$response")
//            Log.d("TEST REQUEST RESULT", "$result")
////            val server = Server("https://horizon-testnet.stellar.org")
////            val account: AccountResponse = server.accounts().account(pair.accountId)
////        println("Balances for account " + pair.accountId)
////            var balances: ArrayList<Array<String>> = ArrayList()
////
////            for (balance in account.balances) {
////                var assetCode = ""
////                if (balance.assetCode.get() != null) {
////                    assetCode = balance.assetCode.get()
////                }
////                balances.add(arrayOf<String>(balance.assetType, assetCode, balance.balance))
////            }
////            for (b in balances) {
////                Log.d("BALANCE asset type", b[0])
////                Log.d("BALANCE asset code", b[1])
////                Log.d("BALANCE asset balance", b[2])
////            }
//        }
////        return balances
//    }
//
//
//
//}