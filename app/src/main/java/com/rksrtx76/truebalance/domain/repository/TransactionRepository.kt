package com.rksrtx76.truebalance.domain.repository

import com.rksrtx76.truebalance.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insertTransaction(transaction : Transaction)
    suspend fun updateTransaction(transaction : Transaction)
    suspend fun deleteTransaction(transaction : Transaction)
    fun getAllTransactions() : Flow<List<Transaction>>
    fun getTransactionsByType(type : String) : Flow<List<Transaction>>
    fun getTransactionsByCategory(category : String) : Flow<List<Transaction>>
    fun getTransactionById(id : Int) : Flow<Transaction?>
    suspend fun deleteTransactionById(id : Int)
    suspend fun deleteAllTransactions()

}