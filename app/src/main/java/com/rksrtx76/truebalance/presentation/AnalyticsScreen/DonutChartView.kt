package com.rksrtx76.truebalance.presentation.AnalyticsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.rksrtx76.truebalance.R
import com.rksrtx76.truebalance.data.model.Transaction
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DonutChartView(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    if (transactions.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_data_available))
        }
        return
    }


    val grouped = transactions.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }


    var othersTotal = 0f
    val slices = mutableListOf<PieChartData.Slice>()

    grouped.forEach { (category, total) ->
        val color = colorForCategory(category)
        if (color == Color(0xFF607D8B)) {
            othersTotal += total.toFloat()
        } else {
            slices.add(
                PieChartData.Slice(
                    label = category,
                    value = total.toFloat(),
                    color = color
                )
            )
        }
    }

    if (othersTotal > 0f) {
        slices.add(
            PieChartData.Slice(
                label = "Others",
                value = othersTotal,
                color = Color(0xFF9C27B0)
            )
        )
    }

    val pieChartData = PieChartData(slices = slices, plotType = PlotType.Donut)
    val pieChartConfig = PieChartConfig(
        showSliceLabels = false,
        isAnimationEnable = true,
        animationDuration = 1200,
        backgroundColor = Color.Transparent
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        slices.forEach { slice ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(slice.color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = slice.label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            modifier = Modifier
                .size(245.dp)
                .aspectRatio(1f)
                .padding(8.dp),
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig
        )
    }
}





fun colorForCategory(category: String): Color {
    return when (category) {
        "FoodsAndDrinks" -> Color(0xFF4CAF50)     // Green
        "Shopping" -> Color(0xFF2196F3)           // Blue
        "Salary" -> Color(0xFFFF9800)             // Orange
        "Entertainment" -> Color(0xFFE91E63)      // Pink
        "Transport" -> Color(0xFF9E9E9E)          // Grey
        "Health" -> Color(0xFFFF5722)             // Deep Orange
        "Bills" -> Color(0xFF3F51B5)              // Indigo
        "Education" -> Color(0xFF00BCD4)          // Cyan
        "Others" -> Color(0xFF9C27B0)             // Purple
        else -> Color(0xFF607D8B)                 // Default: Blue Grey
    }
}

// ------------------------- FILTER HELPERS -------------------------

@RequiresApi(Build.VERSION_CODES.O)
fun getWeeklyTransactions(transactions: List<Transaction>): List<Transaction> {
    val now = LocalDate.now()
    val startOfWeek = now.with(java.time.DayOfWeek.MONDAY)
    val endOfWeek = now.with(java.time.DayOfWeek.SUNDAY)

    return transactions.filter {
        val date = Instant.ofEpochMilli(it.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthlyTransactions(transactions: List<Transaction>): List<Transaction> {
    val now = LocalDate.now()
    return transactions.filter {
        val date = Instant.ofEpochMilli(it.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        date.month == now.month && date.year == now.year
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getYearlyTransactions(transactions: List<Transaction>): List<Transaction> {
    val now = LocalDate.now()
    return transactions.filter {
        val date = Instant.ofEpochMilli(it.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        date.year == now.year
    }
}