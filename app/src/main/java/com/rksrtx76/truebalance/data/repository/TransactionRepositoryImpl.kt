package com.rksrtx76.truebalance.data.repository

import com.rksrtx76.truebalance.data.local.TrueBalanceDatabase
import com.rksrtx76.truebalance.data.model.Transaction
import com.rksrtx76.truebalance.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val trueBalanceDatabase: TrueBalanceDatabase
) : TransactionRepository{
    override suspend fun insertTransaction(transaction: Transaction) = trueBalanceDatabase.getTransactionDao().insertTransaction(transaction)

    override suspend fun updateTransaction(transaction: Transaction) = trueBalanceDatabase.getTransactionDao().updateTransaction(transaction)

    override suspend fun deleteTransaction(transaction: Transaction) = trueBalanceDatabase.getTransactionDao().deleteTransaction(transaction)

    override fun getAllTransactions(): Flow<List<Transaction>> = trueBalanceDatabase.getTransactionDao().getAllTransactions()

    override fun getTransactionsByType(type: String): Flow<List<Transaction>> = if (type == "Overall"){
        getAllTransactions()
    }else{
        trueBalanceDatabase.getTransactionDao().getTransactionsByType(type)
    }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> = trueBalanceDatabase.getTransactionDao().getTransactionsByCategory(category)

    override fun getTransactionById(id: Int): Flow<Transaction?> = trueBalanceDatabase.getTransactionDao().getTransactionById(id)

    override suspend fun deleteTransactionById(id: Int) = trueBalanceDatabase.getTransactionDao().deleteTransactionById(id)

    override suspend fun deleteAllTransactions() = trueBalanceDatabase.getTransactionDao().deleteAllTransactions()
}


