package com.rksrtx76.truebalance.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rksrtx76.truebalance.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction : Transaction)

    @Update
    suspend fun updateTransaction(transaction : Transaction)

    @Delete
    suspend fun deleteTransaction(transaction : Transaction)

    @Query("SELECT * FROM true_balance_transactions ORDER BY createdAt DESC")
    fun getAllTransactions() : Flow<List<Transaction>>

    // Filter by type
    @Query("SELECT * FROM true_balance_transactions WHERE type = :type ORDER BY createdAt DESC")
    fun getTransactionsByType(type : String) : Flow<List<Transaction>>

    // Filter by category
    @Query("SELECT * FROM true_balance_transactions WHERE category = :category ORDER BY createdAt DESC")
    fun getTransactionsByCategory(category : String) : Flow<List<Transaction>>

    // Get a transaction by id
    @Query("SELECT * FROM true_balance_transactions WHERE id = :id")
    fun getTransactionById(id : Int) : Flow<Transaction?>

    @Query("DELETE FROM true_balance_transactions WHERE id = :id")
    suspend fun deleteTransactionById(id : Int)

    @Query("DELETE FROM true_balance_transactions")
    suspend fun deleteAllTransactions()

}