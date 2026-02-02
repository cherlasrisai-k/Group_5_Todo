package com.example.todo.ui.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

fun showDateTimePicker(
    context: Context, onDateTimeSelected: (Long) -> Unit
) {
    val cal = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context, { _, year, month, day ->
            val now = Calendar.getInstance()
            val isToday = year == now.get(Calendar.YEAR) &&
                    month == now.get(Calendar.MONTH) &&
                    day == now.get(Calendar.DAY_OF_MONTH)

            TimePickerDialog(
                context, { _, hour, minute ->
                    val selectedDateTime = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    // For today, check if selected time is in the past.
                    if (isToday && selectedDateTime.timeInMillis < now.timeInMillis) {
                        onDateTimeSelected(now.timeInMillis)
                    } else {
                        onDateTimeSelected(selectedDateTime.timeInMillis)
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            ).show()
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.minDate = cal.timeInMillis
    datePickerDialog.show()
}
