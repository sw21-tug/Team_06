package com.team06.focuswork

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team06.focuswork.ui.splashscreen.SplashScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception


@RunWith(AndroidJUnit4::class)
class SplashInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<SplashScreen> =
        ActivityScenarioRule(SplashScreen::class.java)

    @Test
    fun splashScreenTest() {
        onView(withId(R.id.logoImage)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        try {
            //Passes if AutoLogin Fails
            onView(withId(R.id.login)).check(matches(isDisplayed()))
        } catch (ex: Exception){
            //Fails if AutoLogin happens, so we need to check the main View instead
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        }
    }
}