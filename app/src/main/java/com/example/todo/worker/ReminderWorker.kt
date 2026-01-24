package com.example.todo.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todo.R
import com.example.todo.data.AppDatabase
import kotlinx.coroutines.runBlocking

class ReminderWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val count = runBlocking {
            AppDatabase.get(applicationContext)
                .taskDao().countTodayTasks()
        }

        if (count > 0) showNotification(count)
        return Result.success()
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(count: Int) {

        val channelId = "todo_channel"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Task Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Today's Tasks Reminder")
            .setContentText("You have $count active task(s) today.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(101, notification)
    }
}
