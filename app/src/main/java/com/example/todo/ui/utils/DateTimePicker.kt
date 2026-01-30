package com.example.todo.ui.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

fun showDateTimePicker(
    context: Context, onDateTimeSelected: (Long) -> Unit
) {
    val cal = Calendar.getInstance()

    DatePickerDialog(
        context, { _, year, month, day ->

            val timeCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }

            TimePickerDialog(
                context, { _, hour, minute ->
                    timeCal.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }
                    onDateTimeSelected(timeCal.timeInMillis)
                }, timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE), false
            ).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
    ).show()
}
