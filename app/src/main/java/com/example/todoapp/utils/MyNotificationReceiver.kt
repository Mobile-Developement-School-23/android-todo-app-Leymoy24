package com.example.todoapp.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.R

class MyNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notificationId = intent.getIntExtra("notificationId", 0)
        val message = intent.getStringExtra("message")

        val notificationBuilder = NotificationCompat.Builder(context, "channelId")
            .setContentTitle("Уведомление!")
            .setContentText(message)
            .setSmallIcon(R.drawable.info_outline)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}