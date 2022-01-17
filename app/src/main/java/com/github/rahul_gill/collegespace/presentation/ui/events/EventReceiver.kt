package com.github.rahul_gill.collegespace.presentation.ui.events

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.presentation.ui.MainActivity

const val eventNotificationChannel = "Event Reminders"
const val EVENT_DATA = "eventData"

class EventReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.extras?.get(EVENT_DATA)?.let {
            val eventName = it as String
            val pendingIntent = PendingIntent.getActivity(
                context,
                1,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            context?.let {
                val notificationBuilder =
                    NotificationCompat.Builder(context, eventNotificationChannel)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(eventName)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                NotificationManagerCompat.from(context)
                    .notify(435, notificationBuilder.build())
            }
        }
    }
}