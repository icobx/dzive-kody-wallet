package com.example.dzivekodywallet.data.database

import androidx.room.*
import com.example.dzivekodywallet.data.database.model.Operation

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOperation(operation: Operation)

    @Update
    fun updateOperation(operation: Operation)

    @Delete
    fun deleteOperation(operation: Operation)

    @Query("SELECT * from operation_table WHERE operationId = :operationId")
    fun getOperation(operationId: Long): List<Operation>

    @Query("SELECT DISTINCT transaction_id FROM operation_table WHERE destination_account = :destinationAccount")
    fun getReceivedPayments(destinationAccount: String): List<String>

}
