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

@Database(entities = [Transaction::class, Wallet::class, Contact::class, Balance::class, Operation::class], version = 30, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

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
    }
}