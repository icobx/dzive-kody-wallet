package com.example.dzivekodywallet.data.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dzivekodywallet.data.database.model.*

@Database(entities = [Transaction::class, Wallet::class, Contact::class, Balance::class, Operation::class], version = 25, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

//    abstract val appDatabaseDao: AppDatabaseDao
    abstract val transactionDao: TransactionDao
    abstract val walletDao: WalletDao
    abstract val contactDao: ContactDao
    abstract val balanceDao: BalanceDao
    abstract val operationDao: OperationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database.sql"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

        // Testing
        private fun prepopulateDatabase(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Log.d("app database", "prepopulateDatabase()")

//                    val wallet = Wallet()
//                    wallet.name = "Test wallet"
//                    wallet.balance = 121.5

                    val wallet = ContentValues()
                    wallet.put("walletId", 1)
                    wallet.put("name", "Test wallet 1")
                    wallet.put("balance", 120)

                    db.insert("wallet_table", OnConflictStrategy.IGNORE, wallet)

                    val transaction = ContentValues()
                    transaction.put("transactionId", 1)
                    transaction.put("amount", 121.5)

                    db.insert("transaction_table", OnConflictStrategy.IGNORE, transaction)
                }
            }
        }
    }
}