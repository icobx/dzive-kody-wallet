package com.example.dzivekodywallet

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import retrofit2.http.GET
import retrofit2.http.Path
import org.stellar.sdk.responses.AccountResponse

//import jdk.internal.net.http.common.Pair.pair
import org.stellar.sdk.Server


class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (savedInstanceState == null) {
            val fragment = BalanceFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.wallet_activity, fragment)
                .commit()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        Navigation
            .findNavController(this, R.id.wallet_fragment_container)
            .navigate(R.id.action_balance_fragment_to_walletManagementActivity)
        return true
    }

}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val accId = "GDMUYFD7G3TUG7MJD5NZQ2Q7J3K6XYEDUEUYYENHHOMUZSEVMPQLF3NP"
        val server = Server("https://horizon-testnet.stellar.org")
        val account: AccountResponse = server.accounts().account(accId)

        Log.d("RESPONSE" , "Balances for account $accId")
        for (balance in account.balances) {
            System.out.printf(
                "Type: %s, Code: %s, Balance: %s%n",
                balance.assetType,
                balance.assetCode,
                balance.balance
            )
            Log.d(
                "RESPONSE",
                "type: ${balance.assetType}, code: ${balance.assetCode}, balance: ${balance.balance}"
            )
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}


/* Kotlin data/model classes that map the JSON response, we could also add Moshi
 * annotations to help the compiler with the mappings on a production app */
//data class AccountResponse(val results: List<Account>)
data class Account(
    val last_modified_ledger: Int
)

/* Retrofit service that maps the different endpoints on the API, you'd create one
 * method per endpoint, and use the @Path, @Query and other annotations to customize
 * these at runtime */
interface UserService {
    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: String): Account
}

//fun convertJSONTOData()
//{
//    val gson= Gson()
//    val testObject=gson.fromJson("{\"id\":1,\"name\":\"GSON\"}", Test::class.java);
//    print("Test ID =${testObject.id}")
//}