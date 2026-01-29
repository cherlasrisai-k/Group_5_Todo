package com.example.todo.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.res.stringResource
import com.example.todo.R

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
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Text(stringResource(R.string.HomeScreen_Title), style = MaterialTheme.typography.headlineMedium)

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(5.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(stringResource(R.string.HomeScreen_CardTitle), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.topic,
                onValueChange = vm::onTopicChange,
                label = { Text(stringResource(R.string.HomeScreen_TextFields_Topic)) },
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
                label = { Text(stringResource(R.string.HomeScreen_TextFields_Heading)) },
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

                            val timeCal = Calendar.getInstance()
                            timeCal.set(Calendar.YEAR, y)
                            timeCal.set(Calendar.MONTH, m)
                            timeCal.set(Calendar.DAY_OF_MONTH, d)

                            TimePickerDialog(
                                context,
                                { _, h, min ->
                                    timeCal.set(Calendar.HOUR_OF_DAY, h)
                                    timeCal.set(Calendar.MINUTE, min)
                                    timeCal.set(Calendar.SECOND, 0)

                                    vm.onDateTimeChange(timeCal.timeInMillis)
                                },
                                timeCal.get(Calendar.HOUR_OF_DAY),
                                timeCal.get(Calendar.MINUTE),
                                false
                            ).show()
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(stringResource(R.string.Buttons_SelectDate))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = vm::saveTask,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),

                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.HomeScreen_Buttons_SaveTask))
            }
        }
    }
}
}
}

