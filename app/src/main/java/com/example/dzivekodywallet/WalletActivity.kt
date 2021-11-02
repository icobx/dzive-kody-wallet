package com.example.dzivekodywallet

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import kotlinx.coroutines.*
import retrofit2.http.GET
import retrofit2.http.Path
import org.stellar.sdk.responses.AccountResponse

//import jdk.internal.net.http.common.Pair.pair
import org.stellar.sdk.Server


class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        /* Creates an instance of the UserService using a simple Retrofit builder using Moshi
         * as a JSON converter, this will append the endpoints set on the UserService interface
         * (for example '/api', '/api?results=2') with the base URL set here, resulting on the
//         * full URL that will be called: 'https://randomuser.me/api' */
//        val service = Retrofit.Builder()
//            .baseUrl("https://horizon-testnet.stellar.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(UserService::class.java)

        /* Uses the lifecycle scope to trigger the coroutine. It's important to call this
         * using a scope to follow the structured concurrency principle.
         * https://medium.com/@elizarov/structured-concurrency-722d765aa952
         * https://developer.android.com/topic/libraries/architecture/coroutines */
//        lifecycleScope.launch {
//            Log.d("TEST", "PRED")
//            var pubK = "GDMUYFD7G3TUG7MJD5NZQ2Q7J3K6XYEDUEUYYENHHOMUZSEVMPQLF3NP"
//            val acc = service.getAccount(pubK)
//            /* This will print the result of the network call to the Logcat. This runs on the
//             * main thread */
//            Log.d("ACCOUNT LML:", acc.toString())
//        }

//        val uploadWorkRequest: WorkRequest =
//            OneTimeWorkRequestBuilder<UploadWorker>()
//                .build()
//
//        WorkManager
//            .getInstance(this.applicationContext)
//            .enqueue(uploadWorkRequest)

//        Test.execute()

    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.executeAsyncTask(onPreExecute = {
            // ...
        }, doInBackground = {
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

        }, onPostExecute = {
            // ... here "it" is a data returned from "doInBackground"
        })
    }

    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
            doInBackground()
        }
        onPostExecute(result)
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