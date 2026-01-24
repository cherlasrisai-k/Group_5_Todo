package com.example.todo.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(vm: TaskViewModel, onLogout: () -> Unit) {



    val context = LocalContext.current
    var topic by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Create New Task", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(topic, { topic = it }, label = { Text("Topic") })
        OutlinedTextField(heading, { heading = it }, label = { Text("Heading") })

        Spacer(Modifier.height(8.dp))
        Text(DateFormat.getDateTimeInstance().format(Date(dateTime)))

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
                        }, cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE), false
                    ).show()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) { Text("Pick Date & Time") }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            vm.addTask(topic, heading, dateTime)
            topic = ""
            heading = ""
        }) {
            Text("Save Task")
        }
    }
}
