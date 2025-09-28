package com.rksrtx76.truebalance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rksrtx76.truebalance.data.model.Transaction

@Database(entities = [Transaction::class], version = 2, exportSchema = false)
abstract class TrueBalanceDatabase : RoomDatabase(){
    abstract fun getTransactionDao() : TransactionDao
}



