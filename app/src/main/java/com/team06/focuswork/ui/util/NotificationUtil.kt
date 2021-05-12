package com.team06.focuswork.ui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
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
                        setSound(
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                            audioAttributes
                        )
                    }
                notificationManager.createNotificationChannel(channel)
            }
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
}