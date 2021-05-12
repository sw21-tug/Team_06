package com.team06.focuswork

import android.media.RingtoneManager
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.ui.util.NotificationUtil.getNotificationSound
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotificationUtilInstrumentedTest {

    @Test
    fun getNotificationSoundTest() {
        Assert.assertEquals(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            getNotificationSound(InstrumentationRegistry.getInstrumentation().context, 0)
        )
        Assert.assertEquals(
            Uri.parse("android.resource://" + InstrumentationRegistry.getInstrumentation().context.packageName + "/" + R.raw.notifclassic),
            getNotificationSound(InstrumentationRegistry.getInstrumentation().context, 1)
        )
        Assert.assertEquals(
            Uri.parse("android.resource://" + InstrumentationRegistry.getInstrumentation().context.packageName + "/" + R.raw.notiffunky),
            getNotificationSound(InstrumentationRegistry.getInstrumentation().context, 2)
        )
        Assert.assertEquals(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            getNotificationSound(InstrumentationRegistry.getInstrumentation().context, 123)
        )
    }
}