package com.example.todo.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
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

@Composable
fun HomeScreen(vm: TaskViewModel, onLogout: () -> Unit) {
    val context = LocalContext.current

    var topic by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("To-Do Reminder", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Create New Task", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = heading,
                    onValueChange = { heading = it },
                    label = { Text("Heading") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(DateFormat.getDateTimeInstance().format(Date(dateTime)))

                Spacer(modifier = Modifier.height(8.dp))



                Button(
                    onClick = {
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
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary

                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pick Date & Time")
                }

                Spacer(modifier = Modifier.height(8.dp))


                Button(
                    onClick = {
                        vm.addTask(topic, heading, dateTime)
                        topic = ""
                        heading = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Task")
                }
            }
        }
    }
}
