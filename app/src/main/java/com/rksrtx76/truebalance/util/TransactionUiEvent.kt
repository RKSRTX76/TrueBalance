package com.rksrtx76.truebalance.util

sealed class TransactionUiEvent {
    data class ShowMessage(val message : String) : TransactionUiEvent()
    object TransactionSaved : TransactionUiEvent()
}