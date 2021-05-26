package com.team06.focuswork

import android.media.RingtoneManager
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.ui.util.NotificationUtil.getNotificationChannelId
import com.team06.focuswork.ui.util.NotificationUtil.getNotificationSound
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotificationUtilInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

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

    private fun navigateToSettings() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    private fun chooseNotificationSound(index: Int)
    {
        navigateToSettings()

        Espresso.onView(ViewMatchers.withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText(R.string.notificationSound_title)),
                    ViewActions.click()
                )
            )
        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.notification_sound_entries)
        Espresso.onView(ViewMatchers.withText(array[index]))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click());
    }

    @Test
    fun getNotificationChannelIdTest() {
        val notificationChannelIDs = arrayOf(
            "TIMER_NOTIF_ID_DEFAULT",
            "TIMER_NOTIF_ID_CLASSIC",
            "TIMER_NOTIF_ID_FUNKY"
        )

        for (i in 0..2) {
            chooseNotificationSound(i)
            Assert.assertEquals(
                notificationChannelIDs[i],
                getNotificationChannelId(InstrumentationRegistry.getInstrumentation().targetContext)
            )
        }
    }
}