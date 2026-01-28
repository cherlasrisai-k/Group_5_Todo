package com.example.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        },
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Edit Task", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(topic, { topic = it }, label = { Text("Topic") })
                OutlinedTextField(heading, { heading = it }, label = { Text("Heading") })

                Text(
                    text = DateFormat.getDateTimeInstance().format(Date(dateTime)),
                    style = MaterialTheme.typography.bodyMedium
                )

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
                }) {
                    Text("Pick Date & Time")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextButton(onClick = { onSave(topic, heading, dateTime) }) { Text("Save")
                        TextButton(onClick = onDismiss) { Text("Cancel") }}
                }
            }
        }
    )
}