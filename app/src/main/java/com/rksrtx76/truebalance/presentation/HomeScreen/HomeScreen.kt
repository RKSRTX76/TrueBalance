package com.rksrtx76.truebalance.presentation.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.truebalance.R
import com.rksrtx76.truebalance.data.model.Transaction
import com.rksrtx76.truebalance.presentation.TransactionItem
import com.rksrtx76.truebalance.presentation.TransactionViewModel
import com.rksrtx76.truebalance.ui.theme.AzureBlue
import com.rksrtx76.truebalance.ui.theme.Typography
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel : TransactionViewModel,
    navController: NavController,
    isDarkTheme : Boolean
) {

    val currentHour = remember {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }
    val (greeting, icon) = when(currentHour){
        in 5..11 -> stringResource(R.string.good_morning) to Icons.Filled.WbSunny
        in 12..16 -> stringResource(R.string.good_afternoon) to Icons.Filled.WbSunny
        in 17..20 -> stringResource(R.string.good_evening) to Icons.Filled.NightsStay
        else -> stringResource(R.string.good_night) to Icons.Filled.Bedtime
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        val uiState by viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greeting,
                        style = Typography.bodyMedium,
                        color = Color.White
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector =  icon,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }

            BalanceCard(transactions = uiState.transaction)

            // Recent Transactions
            RecentTransactionSection(
                transactions = uiState.transaction,
                viewModel = viewModel,
                navController = navController,
            )
        }
    }
}

@Composable
fun BalanceCard(
    transactions : List<Transaction>
){
    val (balance, income, expense) = calculateMonthlySummary(transactions)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = AzureBlue
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.total_balance),
                        style = Typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                    )
                    Text(
                        text = "₹ ${"%,.2f".format(balance)}",
                        style = Typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.more)
                    )
                }
            }

            // Bottom row: Income & Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniCardItem(
                    title = stringResource(R.string.income),
                    amount = "₹ ${"%,.2f".format(income)}",
                    icon = Icons.Default.ArrowDownward,
                    iconTint = Color.White
                )
                MiniCardItem(
                    title = stringResource(R.string.expense),
                    amount = "₹ ${"%,.2f".format(expense)}",
                    icon = Icons.Default.ArrowUpward,
                    iconTint = Color.White
                )
            }
        }
    }
}

@Composable
fun MiniCardItem(
    title: String,
    amount: String,
    icon: ImageVector,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = Typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.85f)
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = amount,
            style = Typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun RecentTransactionSection(
    modifier : Modifier = Modifier,
    transactions : List<Transaction>,
    viewModel : TransactionViewModel,
    navController : NavController,
){
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.transaction.isNotEmpty()){
                Text(
                    text = stringResource(R.string.recent_transactions),
                    style = Typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        transactions.take(3).forEach { transaction ->
            TransactionItem(
                transaction = transaction,
                navController = navController
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


fun calculateMonthlySummary(transactions: List<Transaction>): Triple<Double, Double, Double> {
    val now = LocalDate.now()
    val startOfMonth = now.withDayOfMonth(1)
    val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())

    var income = 0.0
    var expense = 0.0

    transactions.forEach { tx ->
        val date = Instant.ofEpochMilli(tx.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        if (!date.isBefore(startOfMonth) && !date.isAfter(endOfMonth)) {
            if (tx.type == "Income") {
                income += tx.amount
            } else {
                expense += tx.amount
            }
        }
    }

    val balance = income - expense
    return Triple(balance, income, expense)
}