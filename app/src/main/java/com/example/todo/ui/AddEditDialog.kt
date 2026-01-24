package com.example.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.todo.data.Task
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun AddEditDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onSave: (String, String, Long) -> Unit
) {
    val context = LocalContext.current
    var topic by remember { mutableStateOf(task?.topic ?: "") }
    var heading by remember { mutableStateOf(task?.heading ?: "") }
    var dateTime by remember { mutableStateOf(task?.dateTime ?: System.currentTimeMillis()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(topic, heading, dateTime) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit Task") },
        text = {
            Column {
                OutlinedTextField(topic, { topic = it }, label = { Text("Topic") })
                OutlinedTextField(heading, { heading = it }, label = { Text("Heading") })

                Text(DateFormat.getDateTimeInstance().format(Date(dateTime)))

                Button(onClick = {
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
                                    dateTime = cal.timeInMillis
                                }, cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE), false
                            ).show()
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) { Text("Pick Date & Time") }
            }
        }
    )
}
