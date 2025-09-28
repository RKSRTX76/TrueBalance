package com.rksrtx76.truebalance.util

import com.rksrtx76.truebalance.data.model.Transaction

data class TransactionUiState(
    val transaction : List<Transaction> = emptyList(),
    val selectedTransaction : Transaction? = null,
    val isLoading : Boolean = false
)
