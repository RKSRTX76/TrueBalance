package com.rksrtx76.truebalance.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector

object TransactionConstants {
    val type = listOf("Income", "Expense")
    val category = listOf(
        "Bill",
        "Debt",
        "Family",
        "Entertainment",
        "Education",
        "FoodsAndDrinks",
        "Healthcare",
        "Housing",
        "Insurance",
        "Personal Spending",
        "Savings",
        "Salary",
        "Shopping",
        "Transportation",
        "Others",
    )

    // Map each category to an icon
    val categoryIcons : Map<String, ImageVector> = mapOf(
        "Bill" to Icons.Outlined.ReceiptLong,
        "Debt" to Icons.Outlined.CreditCard,
        "Family" to Icons.Outlined.Groups,
        "Entertainment" to Icons.Outlined.Movie,
        "Education" to Icons.Outlined.School,
        "FoodsAndDrinks" to Icons.Outlined.Restaurant,
        "Healthcare" to Icons.Outlined.MedicalServices,
        "Housing" to Icons.Outlined.Home,
        "Insurance" to Icons.Outlined.VerifiedUser,
        "Personal Spending" to Icons.Outlined.Person,
        "Savings" to Icons.Outlined.Savings,
        "Salary" to Icons.Outlined.AttachMoney,
        "Shopping" to Icons.Outlined.ShoppingCart,
        "Transportation" to Icons.Outlined.DirectionsCar,
        "Others" to Icons.Outlined.MoreHoriz,
    )

}