package com.example.todo.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(vm: TaskViewModel) {

    val context = LocalContext.current

    var topic by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf(System.currentTimeMillis()) }


    var topicError by remember { mutableStateOf("") }
    var headingError by remember { mutableStateOf("") }
    var timeError by remember { mutableStateOf("") }


    fun validate(): Boolean {
        var ok = true
        topicError = ""
        headingError = ""
        timeError = ""

        if (topic.isBlank()) {
            topicError = "Topic cannot be empty"
            ok = false
        }

        if (heading.isBlank()) {
            headingError = "Heading cannot be empty"
            ok = false
        }

        if (dateTime < System.currentTimeMillis()) {
            timeError = "Please select a future time"
            ok = false
        }

        return ok
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Create New Task", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Topic") },
            isError = topicError.isNotEmpty()
        )
        if (topicError.isNotEmpty())
            Text(topicError, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = heading,
            onValueChange = { heading = it },
            label = { Text("Heading") },
            isError = headingError.isNotEmpty()
        )
        if (headingError.isNotEmpty())
            Text(headingError, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(12.dp))

        Text(DateFormat.getDateTimeInstance().format(Date(dateTime)))
        if (timeError.isNotEmpty())
            Text(timeError, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(6.dp))

        Button(onClick = {
            val cal = Calendar.getInstance()
            android.app.DatePickerDialog(
                context,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    TimePickerDialog(
                        context,
                        { _, h, min ->
                            cal.set(Calendar.HOUR_OF_DAY, h)
                            cal.set(Calendar.MINUTE, min)
                            dateTime = cal.timeInMillis
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
        }) {
            Text("Pick Date & Time")
        }

        Spacer(Modifier.height(14.dp))

        Button(onClick = {
            if (validate()) {
                vm.addTask(topic, heading, dateTime)
                topic = ""
                heading = ""
                dateTime = System.currentTimeMillis()
            }
        }) {
            Text("Save Task")
        }
    }
}
