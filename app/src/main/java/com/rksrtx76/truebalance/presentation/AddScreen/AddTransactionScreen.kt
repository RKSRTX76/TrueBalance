package com.rksrtx76.truebalance.presentation.AddScreen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.truebalance.R
import com.rksrtx76.truebalance.data.model.Transaction
import com.rksrtx76.truebalance.presentation.TransactionViewModel
import com.rksrtx76.truebalance.util.BottomNav
import com.rksrtx76.truebalance.util.Screens
import com.rksrtx76.truebalance.util.TransactionConstants
import com.rksrtx76.truebalance.util.TransactionUiEvent
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel
){
    val uiEvent = viewModel.uiEvent
    val snackbarHostState =  remember{
        SnackbarHostState()
    }
    val context = LocalContext.current
    val controller = LocalSoftwareKeyboardController.current

    // states
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf<String?>(null) }
    var category by remember { mutableStateOf<String?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false)}
    var showTypeDropdown by remember { mutableStateOf(false)}
    var showDatePicker by remember { mutableStateOf(false)}
    var date by remember { mutableStateOf<Long?>(null) }

    val maxInt = 10
    val maxChar = 20

    LaunchedEffect(true) {
        uiEvent.collect { event ->
            when(event){
                is TransactionUiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                TransactionUiEvent.TransactionSaved -> {
                    snackbarHostState.showSnackbar(context.getString(R.string.transaction_saved))
                    // Navigate to main screen
                    navController.navigate(BottomNav.HOME_SCREEN)
                }
            }
        }
    }

    Scaffold(
        snackbarHost =  {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.add_transaction))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screens.HOME_SCREEN){
                                popUpTo(Screens.HOME_SCREEN) { inclusive = true }
                            }
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) {  contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Payment,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.amount),
                    )
                },
                value = amount,
                singleLine = true,
                onValueChange = {
                    if (it.length <= maxInt){
                        amount = it
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Type
            ExposedDropdownMenuBox(
                expanded = showTypeDropdown,
                onExpandedChange = { showTypeDropdown = !showTypeDropdown }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = type ?: "",
                    placeholder = {
                        Text(stringResource(R.string.select_type))
                    },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.transaction_type)) },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showTypeDropdown) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null
                        )
                    }
                )
                DropdownMenu(
                    modifier = Modifier.exposedDropdownSize(),
                    expanded = showTypeDropdown,
                    onDismissRequest = { showTypeDropdown = false }
                ) {
                    TransactionConstants.type.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                type = it
                                if(it == context.getString(R.string.salary)){
                                    category = context.getString(R.string.salary)
                                }else{
                                    category = null
                                }
                                showTypeDropdown = false
                            }
                        )
                    }
                }
            }
            // Category
            ExposedDropdownMenuBox(
                expanded = showCategoryDropdown,
                onExpandedChange = {
                    if(type != "Income"){
                        showCategoryDropdown = !showCategoryDropdown
                    }
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = if(type != stringResource(R.string.income)) category ?: "" else stringResource(R.string.salary),
                    placeholder = {
                        Text(stringResource(R.string.select_category))
                    },
                    onValueChange = { },
                    enabled = type != stringResource(R.string.income),
                    readOnly = true,
                    label = {
                        Text(stringResource(R.string.category))
                    },
                    trailingIcon = {
                            Icon(
                                imageVector = if(showCategoryDropdown) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                                contentDescription = null
                            )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Category,
                            contentDescription = null
                        )
                    }
                )
                if(type != stringResource(R.string.income)){
                    DropdownMenu(
                        modifier = Modifier.exposedDropdownSize(),
                        expanded = showCategoryDropdown,
                        onDismissRequest = {
                            showCategoryDropdown = false
                        }
                    ) {
                        TransactionConstants.category.forEach {
                            DropdownMenuItem(
                                text = { Text(it)},
                                onClick = {
                                    category =  it
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }else{
                    category = stringResource(R.string.salary)
                }
            }
            // Date
            ExposedDropdownMenuBox(
                expanded = showDatePicker,
                onExpandedChange = { showDatePicker = !showDatePicker}
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = date?.let { DateFormat.getDateInstance().format(Date(it)) } ?: "",
                    placeholder = {
                        Text(stringResource(R.string.select_date))
                    },
                    onValueChange = { },
                    label = {
                        Text(
                            stringResource(R.string.select_date)
                        )
                    },
                    readOnly = true,
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                showDatePicker =  true
                            }
                        ) {
                            Icon(imageVector = Icons.Outlined.Today,contentDescription = null)
                        }
                    }
                )
            }
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    date = it
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text(stringResource(R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false}
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = note,
                onValueChange = {
                    if(it.length <= maxChar){
                        note = it
                    }
                },
                label = {
                    Text(stringResource(R.string.add_note))
                },
                trailingIcon = {
                    Text("${note.length}/$maxChar", textAlign = TextAlign.End)
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.StickyNote2,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Transaction
            Button(
                onClick = {
                    val transaction = Transaction(
                        title = "",
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        type = type!!,
                        category = category!!,
                        date = date!!,
                        note = note
                    )
                    viewModel.addTransaction(transaction)
                    controller?.hide()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = amount.isNotEmpty() && type != null && category != null && date != null
            ) {
                Text(stringResource(R.string.save))
            }

            BackHandler {
                navController.navigate(Screens.HOME_SCREEN){
                    popUpTo(Screens.HOME_SCREEN) { inclusive = true }
                }
            }
        }
    }
}