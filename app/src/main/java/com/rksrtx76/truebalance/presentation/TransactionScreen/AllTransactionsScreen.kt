package com.rksrtx76.truebalance.presentation.TransactionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.truebalance.R
import com.rksrtx76.truebalance.data.model.Transaction
import com.rksrtx76.truebalance.presentation.TransactionItem
import com.rksrtx76.truebalance.presentation.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreen(
    navController: NavController,
    viewModel: TransactionViewModel,
    paddingValues: PaddingValues
){
    val uiState by viewModel.uiState.collectAsState()
    val groupedTransactions = remember(uiState.transaction) {
        groupTransactionsByDate(uiState.transaction)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.all_transactions)) },
            )
        }
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            if (uiState.transaction.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    groupedTransactions.forEach { (dateHeader, transactions) ->
                        item{
                            Text(
                                text = dateHeader,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(transactions){ transaction ->
                            TransactionItem(
                                transaction = transaction,
                                navController = navController
                            )
                        }
                    }
                }
            }else{
                Text(
                    text = stringResource(R.string.no_transactions_found),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}



fun groupTransactionsByDate(
    transactions : List<Transaction>
) : Map<String, List<Transaction>> {

    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    val dateFormat = SimpleDateFormat("MMM dd, yyy", Locale.getDefault())

    return transactions.groupBy { transaction ->
        val transactionDate = Calendar.getInstance().apply {
            time = Date(transaction.date)
        }
        when{
            isSameDay(transactionDate, today) -> "Today"
            isSameDay(transactionDate, yesterday) -> "Yesterday"
            else -> dateFormat.format(transactionDate.time)
        }
    }
}

private fun isSameDay(
    calendar1 : Calendar, calendar2: Calendar
): Boolean{
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

