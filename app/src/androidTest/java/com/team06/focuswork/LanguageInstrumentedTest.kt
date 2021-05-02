package com.team06.focuswork

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class LanguageInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {

    }

    @Suppress("DEPRECATION")
    private fun setLocale(language: String, country: String) {
        //parts taken from https://stackoverflow.com/questions/16760194/locale-during-unit-test-on-android

        val locale = Locale(language, country)
        // here we update locale for date formatters
        Locale.setDefault(locale)
        // here we update locale for app resources

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val res: Resources = context.resources
        val config: Configuration = res.configuration
        config.setLocale(locale)

        res.updateConfiguration(
            config,
            res.displayMetrics
        )

    }

    @Test
    fun chineseTest() {
        //This is needed since accessing R.strings.menu_overview will always get the displayed text
        //However, we want to know whether Chinese is currently being displayed.
        var text = "概述"
        setLocale("zh", "CN")

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val vg: ViewGroup? =
            getActivity(context)?.findViewById(R.id.fragment_container_overview)
        vg?.invalidate()

        Assert.assertEquals(text, context.getString(R.string.menu_overview))
        /*
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        Thread.sleep(400)
        onView(withId(R.id.nav_overview))
                .check(matches(withText(text)))
         */

    }

    @Test
    fun russianTest() {
        //This is needed since accessing R.strings.menu_overview will always get the displayed text
        //However, we want to know whether Russian is currently being displayed.
        var text = "обзор"
        setLocale("ru", "RU")

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val vg: ViewGroup? =
                getActivity(context)?.findViewById(R.id.fragment_container_overview)
        vg?.invalidate()

        Assert.assertEquals(text, context.getString(R.string.menu_overview))

    }
}