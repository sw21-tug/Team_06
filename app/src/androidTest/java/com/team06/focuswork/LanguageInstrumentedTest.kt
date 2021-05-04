package com.team06.focuswork

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class LanguageInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun cleanUp() {
        // reset the locale to English
        setLocale("en", "US")
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

    private fun assertLanguageIsEnglish()
    {
        //This is needed since accessing R.strings.menu_overview will always get the displayed text
        //However, we want to know whether English is currently being displayed.
        var text = "Overview"

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

        Assert.assertEquals(text, context.getString(R.string.menu_overview))
    }
    private fun assertLanguageIsChinese()
    {
        //This is needed since accessing R.strings.menu_overview will always get the displayed text
        //However, we want to know whether Chinese is currently being displayed.
        var text = "概述"

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

        Assert.assertEquals(text, context.getString(R.string.menu_overview))
    }
    private fun assertLanguageIsRussian()
    {
        //This is needed since accessing R.strings.menu_overview will always get the displayed text
        //However, we want to know whether Russian is currently being displayed.
        var text = "обзор"

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

        Assert.assertEquals(text, context.getString(R.string.menu_overview))
    }

    @Test
    fun englishLocaleTest() {
        setLocale("en", "US")
        assertLanguageIsEnglish()
    }

    @Test
    fun chineseLocaleTest() {
        setLocale("zh", "CN")
        assertLanguageIsChinese()
    }

    @Test
    fun russianLocaleTest() {
        setLocale("ru", "RU")
        assertLanguageIsRussian()
    }

    private fun navigateToOverview() {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview));
    }
    private fun navigateToSettings() {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    @Test
    fun settingsSelectEnglishTest() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(R.string.language_title)), click()))

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.language_entries)

        onView(withText(array[0]))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click());

        navigateToOverview()
        onView(withId(R.id.toolbar))
            .check(matches(hasDescendant(withText("Overview"))))
    }

    @Test
    fun settingsSelectRussianTest() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(R.string.language_title)), click()))

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.language_entries)

        onView(withText(array[1]))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click());

        navigateToOverview()
        onView(withId(R.id.toolbar))
            .check(matches(hasDescendant(withText("обзор"))))
    }

    @Test
    fun settingsSelectChineseTest() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(R.string.language_title)), click()))

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.language_entries)

        onView(withText(array[2]))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click());

        navigateToOverview()
        onView(withId(R.id.toolbar))
            .check(matches(hasDescendant(withText("概述"))))
    }
}