package com.rksrtx76.truebalance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rksrtx76.truebalance.data.model.Transaction
import com.rksrtx76.truebalance.domain.repository.TransactionRepository
import com.rksrtx76.truebalance.util.TransactionUiEvent
import com.rksrtx76.truebalance.util.TransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState : StateFlow<TransactionUiState> = _uiState

    // One time events like snack bar
    private val _uiEvent = MutableSharedFlow<TransactionUiEvent>()
    val uiEvent : SharedFlow<TransactionUiEvent> = _uiEvent

    init {
        loadAllTransactions()
    }

    private fun loadAllTransactions(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // collect() will keep listening for database changes
            repository.getAllTransactions().collect { transaction->
                _uiState.value = _uiState.value.copy(
                    transaction = transaction,
                    isLoading = false
                )
            }
        }
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch {
            try {
                repository.insertTransaction(transaction)
                _uiEvent.emit(TransactionUiEvent.TransactionSaved)
            }catch (e : Exception){
                _uiEvent.emit(TransactionUiEvent.ShowMessage(e.message ?: "Insert failed"))
            }
        }
    }

    // delete a single transaction
    fun deleteTransaction(id: Int){
        viewModelScope.launch {
            try {
                repository.deleteTransactionById(id)
                _uiEvent.emit(TransactionUiEvent.ShowMessage("Transaction deleted"))
            }catch (e : Exception){
                _uiEvent.emit(TransactionUiEvent.ShowMessage(e.message ?: "Delete failed"))
            }
        }
    }
    fun deleteAllTransactions() {
        viewModelScope.launch {
            try {
                repository.deleteAllTransactions()
                _uiEvent.emit(TransactionUiEvent.ShowMessage("All transactions deleted"))
            } catch (e: Exception) {
                _uiEvent.emit(TransactionUiEvent.ShowMessage(e.message ?: "Delete failed"))
            }
        }
    }

    fun getTransactionById(id : Int){
        viewModelScope.launch {
            repository.getTransactionById(id).collect { transaction->
                _uiState.value = _uiState.value.copy(selectedTransaction = transaction)
            }
        }
    }

}