package com.team06.focuswork

import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.net.Uri
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.ui.util.NotificationUtil.getNotificationChannelId
import com.team06.focuswork.ui.util.NotificationUtil.getNotificationSound
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotificationUtilInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val navigator = NavigationUtil()
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val resPath = "android.resource://" + context.packageName + "/"

    @Test
    fun getNotificationSoundTest() {
        assertEquals(getDefaultUri(TYPE_NOTIFICATION), getNotificationSound(context, 0))
        assertEquals(Uri.parse(resPath + R.raw.notifclassic), getNotificationSound(context, 1))
        assertEquals(Uri.parse(resPath + R.raw.notiffunky), getNotificationSound(context, 2))
        assertEquals(getDefaultUri(TYPE_NOTIFICATION), getNotificationSound(context, 123))
    }

    private fun chooseNotificationSound(index: Int) {
        val sound =
            targetContext.resources.getStringArray(R.array.notification_sound_entries)[index]
        navigator.chooseSetting(R.string.notificationSound_title, sound)
    }

    @Test
    fun getNotificationChannelIdTest() {
        arrayOf(
            "TIMER_NOTIF_ID_DEFAULT",
            "TIMER_NOTIF_ID_CLASSIC",
            "TIMER_NOTIF_ID_FUNKY"
        ).forEachIndexed { index, notificationChannelId ->
            chooseNotificationSound(index)
            assertEquals(notificationChannelId, getNotificationChannelId(targetContext))
        }
    }
}