package com.artisticent.collegespace.presentation.ui.events

import android.R.drawable
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.artisticent.collegespace.presentation.ui.MainActivity


private const val eventNotificationChannel = "Event Reminders"

fun setupNotificationChannels(context: Context){
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(eventNotificationChannel, eventNotificationChannel, importance)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun triggerEventNotification(
    context: Context,
    eventName: String,
    
) {
    val notificationClickIntent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 1, notificationClickIntent,FLAG_UPDATE_CURRENT)
    val notificationTitle = "A sample notification"
    val notificationText = "This is sample notification body text"
    val notificationBuilder =
        NotificationCompat.Builder(context, eventNotificationChannel)
            .setSmallIcon(drawable.ic_dialog_alert)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


    val notificationManager =
        NotificationManagerCompat.from(context)
    notificationManager.notify(435, notificationBuilder.build())

}