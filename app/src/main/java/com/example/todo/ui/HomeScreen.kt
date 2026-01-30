package com.example.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen(vm: TaskViewModel, activity: ComponentActivity) {

    val state by vm.homeState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current

    LaunchedEffect(Unit) {
        vm.events.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val textFieldBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val windowSizeClass = calculateWindowSizeClass(activity)

    val cardWidthMultiplier = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1f
        WindowWidthSizeClass.Medium -> 0.7f
        WindowWidthSizeClass.Expanded -> 0.5f
        else -> 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(cardWidthMultiplier)
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
                Text(
                    stringResource(R.string.HomeScreen_CardTitle),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.topic,
                    onValueChange = vm::onTopicChange,
                    label = { Text(stringResource(R.string.HomeScreen_TextFields_Topic)) },
                    isError = state.topicError.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = textFieldBorderColor,
                        focusedLabelColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        unfocusedLabelColor = textFieldBorderColor,
                        cursorColor = textFieldBorderColor,
                        focusedTextColor = textFieldBorderColor,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    )
                )

                if (state.topicError.isNotEmpty()) {
                    Text(
                        text = state.topicError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.heading,
                    onValueChange = vm::onHeadingChange,
                    label = { Text(stringResource(R.string.HomeScreen_TextFields_Heading)) },
                    isError = state.headingError.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = textFieldBorderColor,
                        focusedLabelColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        unfocusedLabelColor = textFieldBorderColor,
                        cursorColor = textFieldBorderColor,
                        focusedTextColor = textFieldBorderColor,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    )
                )

                if (state.headingError.isNotEmpty()) {
                    Text(
                        text = state.headingError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
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

                OutlinedButton(
                    onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                val timeCal = Calendar.getInstance()
                                timeCal.set(y, m, d)
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
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.Buttons_SelectDate))
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = vm::saveTask,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.HomeScreen_Buttons_SaveTask))
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { snackbarData ->
            Snackbar(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = snackbarData.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}