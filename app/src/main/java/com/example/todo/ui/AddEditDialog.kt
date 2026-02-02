package com.example.todo.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.ui.utils.showDateTimePicker
import com.example.todo.viewmodel.TasksViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun AddEditDialog(vm: TasksViewModel) {

    val state by vm.addEditState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    AlertDialog(onDismissRequest = vm::onDialogDismiss, confirmButton = {}, title = {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.EditDialog_Title), fontWeight = FontWeight.Bold)
        }
    }, text = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val focusManager = LocalFocusManager.current

            val textFieldBorderColor = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }

            OutlinedTextField(
                value = state.topic,
                onValueChange = vm::onEditTopicChange,
                label = { Text(stringResource(R.string.EditDialog_Topic)) },
                isError = state.topicError.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = textFieldBorderColor,
                    focusedLabelColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor,
                    unfocusedLabelColor = textFieldBorderColor,
                    cursorColor = textFieldBorderColor,
                    focusedTextColor = textFieldBorderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.topicError.isNotEmpty()) Text(
                state.topicError, color = MaterialTheme.colorScheme.error
            )

            OutlinedTextField(
                value = state.heading,
                onValueChange = vm::onEditHeadingChange,
                label = { Text(stringResource(R.string.EditDialog_Heading)) },
                isError = state.headingError.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = textFieldBorderColor,
                    focusedLabelColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor,
                    unfocusedLabelColor = textFieldBorderColor,
                    cursorColor = textFieldBorderColor,
                    focusedTextColor = textFieldBorderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
            )
            if (state.headingError.isNotEmpty()) Text(
                state.headingError, color = MaterialTheme.colorScheme.error
            )

            Text(DateFormat.getDateTimeInstance().format(Date(state.dateTime)))
            if (state.timeError.isNotEmpty()) {
                Text(state.timeError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    showDateTimePicker(context) { selectedMillis ->
                        vm.onEditDateTimeChange(selectedMillis)
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.Buttons_SelectDate))
            }


            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = vm::updateEditedTask) { Text(stringResource(R.string.Buttons_Save)) }
                TextButton(onClick = vm::onDialogDismiss) { Text(stringResource(R.string.Buttons_Cancel)) }
            }
        }
    })
}
