package pl.wsei.pam.lab07.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.wsei.pam.lab07.data.Priority
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: FormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.todoTaskUiState
    val coroutineScope = rememberCoroutineScope()
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.todoTask.deadline.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.updateUiState(uiState.todoTask.copy(deadline = date))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "New Task",
                showBackIcon = true,
                route = "list",
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.save()
                        navController.navigate("list")
                    }
                },
                isSaveEnabled = uiState.isValid
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.todoTask.title,
                    onValueChange = { viewModel.updateUiState(uiState.todoTask.copy(title = it)) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.todoTask.title.isBlank()
                )

                Text(text = "Priority", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Priority.entries.forEach { priority ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = uiState.todoTask.priority == priority,
                                onClick = { viewModel.updateUiState(uiState.todoTask.copy(priority = priority)) }
                            )
                            Text(text = priority.name)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Is Done?")
                    Checkbox(
                        checked = uiState.todoTask.isDone,
                        onCheckedChange = { viewModel.updateUiState(uiState.todoTask.copy(isDone = it)) }
                    )
                }

                Text(text = "Deadline: ${uiState.todoTask.deadline}", style = MaterialTheme.typography.titleMedium)
                Button(onClick = { showDatePicker = true }) {
                    Text("Select Date")
                }
            }
        }
    )
}
