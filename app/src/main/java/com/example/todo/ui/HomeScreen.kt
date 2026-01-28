package com.example.todo.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

//@Composable
//fun HomeScreen(vm: TaskViewModel, onLogout: () -> Unit) {
//    val context = LocalContext.current
//
//    // State for inputs
//    var topic by rememberSaveable { mutableStateOf("") }
//    var heading by rememberSaveable { mutableStateOf("") }
//    var dateTime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
//
//    var topicError by remember { mutableStateOf("") }
//    var headingError by remember { mutableStateOf("") }
//    var timeError by remember { mutableStateOf("") }
//
//    val scrollState = rememberScrollState()
//
//    // Validation Logic
//    fun validate(): Boolean {
//        var isValid = true
//        topicError = ""
//        headingError = ""
//        timeError = ""
//
//        if (topic.isBlank()) {
//            topicError = "Topic cannot be empty"
//            isValid = false
//        }
//
//        if (heading.isBlank()) {
//            headingError = "Heading cannot be empty"
//            isValid = false
//        }
//
//        // Check if selected time is at least 1 minute in the future
//        if (dateTime <= System.currentTimeMillis()) {
//            timeError = "Please select a future time"
//            isValid = false
//        }
//
//        return isValid
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("To-Do Reminder", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .padding(16.dp),
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
//            elevation = CardDefaults.cardElevation(5.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text("Create New Task", style = MaterialTheme.typography.headlineSmall)
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Topic Field
//                OutlinedTextField(
//                    value = topic,
//                    onValueChange = {
//                        topic = it
//                        if (it.isNotBlank()) topicError = ""
//                    },
//                    label = { Text("Topic") },
//                    isError = topicError.isNotEmpty(),
//                    modifier = Modifier.fillMaxWidth()
//                )
//                if (topicError.isNotEmpty()) {
//                    Text(
//                        text = topicError,
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Heading Field
//                OutlinedTextField(
//                    value = heading,
//                    onValueChange = {
//                        heading = it
//                        if (it.isNotBlank()) headingError = ""
//                    },
//                    label = { Text("Heading") },
//                    isError = headingError.isNotEmpty(),
//                    modifier = Modifier.fillMaxWidth()
//                )
//                if (headingError.isNotEmpty()) {
//                    Text(
//                        text = headingError,
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Date/Time Display
//                Text(
//                    text = DateFormat.getDateTimeInstance().format(Date(dateTime)),
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = if (timeError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
//                )
//                if (timeError.isNotEmpty()) {
//                    Text(
//                        text = timeError,
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Date Picker Button
//                Button(
//                    onClick = {
//                        val cal = Calendar.getInstance()
//                        android.app.DatePickerDialog(
//                            context,
//                            { _, y, m, d ->
//                                cal.set(y, m, d)
//                                TimePickerDialog(
//                                    context,
//                                    { _, h, min ->
//                                        cal.set(Calendar.HOUR_OF_DAY, h)
//                                        cal.set(Calendar.MINUTE, min)
//                                        dateTime = cal.timeInMillis
//                                        timeError = "" // Clear error on selection
//                                    },
//                                    cal.get(Calendar.HOUR_OF_DAY),
//                                    cal.get(Calendar.MINUTE),
//                                    false
//                                ).show()
//                            },
//                            cal.get(Calendar.YEAR),
//                            cal.get(Calendar.MONTH),
//                            cal.get(Calendar.DAY_OF_MONTH)
//                        ).show()
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                    )
//                ) {
//                    Text("Pick Date & Time")
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Save Button
//                Button(
//                    onClick = {
//                        if (validate()) {
//                            vm.addTask(topic, heading, dateTime)
//                            topic = ""
//                            heading = ""
//                            dateTime = System.currentTimeMillis()
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary,
//                        contentColor = MaterialTheme.colorScheme.onPrimary
//                    )
//                ) {
//                    Text("Save Task")
//                }
//            }
//        }
//
//
//    }
//}

import android.app.DatePickerDialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import java.util.*

@Composable
fun HomeScreen(vm: TaskViewModel) {

    val state by vm.homeState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect one-time events
    LaunchedEffect(Unit) {
        vm.events.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Text("To-Do Reminder", style = MaterialTheme.typography.headlineMedium)

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Create New Task", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.topic,
                onValueChange = vm::onTopicChange,
                label = { Text("Topic") },
                isError = state.topicError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.topicError.isNotEmpty()) {
                Text(state.topicError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.heading,
                onValueChange = vm::onHeadingChange,
                label = { Text("Heading") },
                isError = state.headingError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.headingError.isNotEmpty()) {
                Text(state.headingError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = DateFormat.getDateTimeInstance().format(Date(state.dateTime)),
                style = MaterialTheme.typography.bodyLarge,
                color = if (state.timeError.isNotEmpty())
                    MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )

            if (state.timeError.isNotEmpty()) {
                Text(state.timeError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            cal.set(y, m, d)
                            TimePickerDialog(
                                context,
                                { _, h, min ->
                                    cal.set(Calendar.HOUR_OF_DAY, h)
                                    cal.set(Calendar.MINUTE, min)
                                    vm.onDateTimeChange(cal.timeInMillis)
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                false
                            ).show()
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Date & Time")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = vm::saveTask,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}
}
}

