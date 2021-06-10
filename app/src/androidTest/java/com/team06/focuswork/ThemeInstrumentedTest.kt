package com.team06.focuswork

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.espressoUtil.ThemeUtil.withAccentColor
import com.team06.focuswork.espressoUtil.ThemeUtil.withBackgroundColor
import com.team06.focuswork.model.LoggedInUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ThemeInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val navigator = NavigationUtil()
    private val user = LoggedInUser("SfuvPQ8Uf2wistKapXBQ")
    private val backgrounds = InstrumentationRegistry.getInstrumentation()
        .targetContext.resources.getStringArray(R.array.color_background_entries)
    private val accents = InstrumentationRegistry.getInstrumentation()
        .targetContext.resources.getStringArray(R.array.color_accent_entries)

    private val backgroundColourDark = 0xFF232323.toInt()
    private val backgroundColourLight = 0xFFE9E9E9.toInt()
    private val accentColourRed = 0xFFED413E.toInt()
    private val accentColourBlue = 0xFF4E84F5.toInt()

    @Before
    fun init() {
        MockUtil.mockUser(user)
    }

    @Test
    fun testDarkBackground() {
        navigator.chooseSetting(R.string.colorBackground_title, backgrounds[1])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.recycler_view)).check(matches(withBackgroundColor(backgroundColourDark)))
    }

    @Test
    fun testLightBackground() {
        navigator.chooseSetting(R.string.colorBackground_title, backgrounds[0])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.recycler_view)).check(matches(withBackgroundColor(backgroundColourLight)))
    }

    @Test
    fun testRedAccent() {
        navigator.chooseSetting(R.string.colorAccent_title, accents[0])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.toolbar)).check(matches(withAccentColor(accentColourRed)))
    }

    @Test
    fun testBlueAccent() {
        navigator.chooseSetting(R.string.colorAccent_title, accents[1])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.toolbar)).check(matches(withAccentColor(accentColourBlue)))
    }
}