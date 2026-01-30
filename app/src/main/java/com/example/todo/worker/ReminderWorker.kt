package com.example.todo.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todo.MainActivity
import com.example.todo.R


class ReminderWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        // Get task details passed from ViewModel
        val topic = inputData.getString("topic") ?: return Result.failure()
        val heading = inputData.getString("heading") ?: ""

        showNotification(topic, heading)

        return Result.success()
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(topic: String, heading: String) {

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


        val intent = Intent(
            Intent.ACTION_VIEW,
            "todo://active_tasks".toUri(),
            applicationContext,
            MainActivity::class.java
        )


        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hurry Up! Task Reminder: $topic")
            .setContentText(heading)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(101, notification)
    }
}
