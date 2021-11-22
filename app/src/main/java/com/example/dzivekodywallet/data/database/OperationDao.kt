package com.example.dzivekodywallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Operation

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOperation(operation: Operation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperations(vararg operation: Operation)

    @Update
    fun updateOperation(operation: Operation)

    @Query("Select * from operation_table")
    fun getOperations(): LiveData<List<Operation>>

    @Delete
    fun deleteOperation(operation: Operation)

    @Query("SELECT * from operation_table WHERE operationId = :operationId")
    fun getOperation(operationId: Long): List<Operation>

    @Query("SELECT * from operation_table WHERE transaction_id = :transactionId")
    fun getOperationsForTransaction(transactionId: String): LiveData<List<Operation>>

    @Query("SELECT DISTINCT transaction_id FROM operation_table WHERE destination_account = :destinationAccount")
    fun getReceivedPayments(destinationAccount: String): LiveData<List<String>>

}
