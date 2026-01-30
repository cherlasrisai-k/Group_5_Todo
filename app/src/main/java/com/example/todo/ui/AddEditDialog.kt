package com.example.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.*

@Composable
fun AddEditDialog(vm: TaskViewModel) {

    val state by vm.addEditState.collectAsState()
    val context = LocalContext.current
    var scrollState= rememberScrollState()

    AlertDialog(
        onDismissRequest = vm::onDialogDismiss,
        confirmButton = {},
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.EditDialog_Title), fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = state.topic,
                    onValueChange = vm::onEditTopicChange,
                    label = { Text(stringResource(R.string.EditDialog_Topic)) },
                    isError = state.topicError.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.topicError.isNotEmpty())
                    Text(state.topicError, color = MaterialTheme.colorScheme.error)

                OutlinedTextField(
                    value = state.heading,
                    onValueChange = vm::onEditHeadingChange,
                    label = { Text(stringResource(R.string.EditDialog_Heading)) },
                    isError = state.headingError.isNotEmpty(),
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        errorBorderColor = MaterialTheme.colorScheme.error),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                )
                if (state.headingError.isNotEmpty())
                    Text(state.headingError, color = MaterialTheme.colorScheme.error)

                Text(DateFormat.getDateTimeInstance().format(Date(state.dateTime)))

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

                                        vm.onEditDateTimeChange(timeCal.timeInMillis)
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.Buttons_SelectDate))
                }


                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = vm::updateEditedTask) { Text(stringResource(R.string.Buttons_Save)) }
                    TextButton(onClick = vm::onDialogDismiss) { Text(stringResource(R.string.Buttons_Cancel)) }
                }
            }
        }
    )
}
