package com.rksrtx76.truebalance.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.DateFormat

@Entity(tableName = "true_balance_transactions")
data class Transaction (

    @ColumnInfo(name = "title")
    val title : String,
    @ColumnInfo(name = "amount")
    val amount : Double,
    @ColumnInfo(name = "type")
    val type : String,
    @ColumnInfo(name = "category")
    val category : String,
    @ColumnInfo(name = "date")
    val date : Long,
    @ColumnInfo(name = "note")
    val note : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int = 0,

    ) : Serializable {
    // Date format : Jan 12, 2025, 12:00 AM
    val createdAtDateFormat : String
        get() = DateFormat.getDateTimeInstance()
            .format(createdAt)

}