package com.team06.focuswork

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.NavigationUtil
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class LanguageInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private var context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val navigator = NavigationUtil()

    @After
    fun cleanUp() {
        setLocale("en", "US")
        Thread.sleep(200)
        navigateToLanguageSettings()

        val languages = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.language_entries)
        onView(withText(languages[0])).inRoot(isDialog()).check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun englishLocaleTest() {
        setLocale("en", "US")
        Assert.assertEquals("Overview", context.getString(R.string.menu_overview))
    }

    @Test
    fun chineseLocaleTest() {
        setLocale("zh", "CN")
        Assert.assertEquals("概述", context.getString(R.string.menu_overview))
    }

    @Test
    fun russianLocaleTest() {
        setLocale("ru", "RU")
        Assert.assertEquals("обзор", context.getString(R.string.menu_overview))
    }

    private fun setLocale(language: String, country: String) {
        //parts taken from https://stackoverflow.com/questions/16760194/locale-during-unit-test-on-android
        val locale = Locale(language, country) // here we update locale for date formatters
        Locale.setDefault(locale) // here we update locale for app resources

        val res: Resources = context.resources
        res.configuration.setLocale(locale)
        context = context.createConfigurationContext(res.configuration)
    }

    private fun navigateToLanguageSettings() {
        navigator.navigateToSettings()
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.language_title)), click()
                )
            )
    }

    @Test
    fun settingsSelectEnglishTest() {
        navigateToLanguageSettings()
        val array = context.resources.getStringArray(R.array.language_entries)
        onView(withText(array[0])).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Overview"))))
    }

    @Test
    fun settingsSelectRussianTest() {
        navigateToLanguageSettings()
        val array = context.resources.getStringArray(R.array.language_entries)
        onView(withText(array[1])).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("обзор"))))
    }

    @Test
    fun settingsSelectChineseTest() {
        navigateToLanguageSettings()
        val array = context.resources.getStringArray(R.array.language_entries)
        onView(withText(array[2])).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("概述"))))
    }
}