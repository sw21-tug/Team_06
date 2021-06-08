package com.team06.focuswork.ui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R

object NotificationUtil {
    //The channel names have to be in the same order as the values/entries in the settings
    private val NOTIFICATION_CHANNEL_IDS = arrayOf(
        "TIMER_NOTIF_ID_DEFAULT",
        "TIMER_NOTIF_ID_CLASSIC",
        "TIMER_NOTIF_ID_FUNKY"
    )

    fun createNotifChannels(context: Context) {
        // based off this tutorial
        // https://www.youtube.com/watch?v=B5dgmvbrHgs

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer finished"
            val descriptionText = "The timer for your task has finished."
            val important = NotificationManager.IMPORTANCE_DEFAULT

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            // We need to create a channel for each sound,
            // because once channel is created, the sound cannot be changed
            for (i in NOTIFICATION_CHANNEL_IDS.indices) {
                val channel =
                    NotificationChannel(NOTIFICATION_CHANNEL_IDS[i], name, important).apply {
                        description = descriptionText
                        setSound(getNotificationSound(context, i), audioAttributes)
                    }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun getNotificationSound(context: Context, notificationSoundIndex: Int): Uri {
        return when (notificationSoundIndex) {
            0 -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            1 -> Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notifclassic)
            2 -> Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notiffunky)
            else -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
    }

    fun getNotificationChannelId(context: Context): String {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val notificationSound =
            (preferences.getString("notificationSound", "default")).toString()

        val notificationSoundValues =
            context.resources.getStringArray(R.array.notification_sound_values)
        for (i in NOTIFICATION_CHANNEL_IDS.indices) {
            if (notificationSound == notificationSoundValues[i])
                return NOTIFICATION_CHANNEL_IDS[i]
        }

        // If nothing found, return default channel
        return NOTIFICATION_CHANNEL_IDS[0]
    }

    fun sendTimerFinishedNotif(context: Context) {
        // based off this tutorial
        // navigates back to app by clicking on it
        // https://www.youtube.com/watch?v=B5dgmvbrHgs
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(
            context,
            getNotificationChannelId(context)
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_message))
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.notification_message)
                )
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(101, builder.build())
        }
    }
}