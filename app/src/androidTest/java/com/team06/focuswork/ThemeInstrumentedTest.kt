package com.team06.focuswork

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.util.Checks
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.espressoUtil.ThemeUtil
import com.team06.focuswork.espressoUtil.ThemeUtil.withBackgroundColor
import com.team06.focuswork.model.LoggedInUser
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class ThemeInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val navigator = NavigationUtil()
    private val valueSetter = PrepareValuesUtil()
    private val user = LoggedInUser("SfuvPQ8Uf2wistKapXBQ")
    private val backgrounds = InstrumentationRegistry.getInstrumentation()
        .targetContext.resources.getStringArray(R.array.color_background_entries)
    private val accents = InstrumentationRegistry.getInstrumentation()
        .targetContext.resources.getStringArray(R.array.color_accent_entries)

    private val backgroundColourDark = 0xFF232323.toInt()
    private val backgroundColourLight = 0xFFE9E9E9.toInt()

    @Before
    fun init() {
        MockUtil.mockUser(user)
    }

    @Test
    fun testDarkBackground() {
        navigator.chooseSetting(R.string.colorBackground_title, backgrounds[1])
        //navigator.chooseSetting(R.string.colorAccent_title, accents[1])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.recycler_view)).check(matches(withBackgroundColor(backgroundColourDark)))
    }

    @Test
    fun testLightBackground() {
        navigator.chooseSetting(R.string.colorBackground_title, backgrounds[0])
        //navigator.chooseSetting(R.string.colorAccent_title, accents[1])
        navigator.navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.recycler_view)).check(matches(withBackgroundColor(backgroundColourLight)))
    }
}