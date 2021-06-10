package com.team06.focuswork

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.ui.login.LoginActivity
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    private var navigator = NavigationUtil()
    private var context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val valueSetter = PrepareValuesUtil()

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun logOut() {
        try {
            //If this passes, we are in the Login Screen!
            onView(withId(R.id.login)).check(matches(isDisplayed()))
        } catch (ex: Exception) {
            //If this fails, AutoLogin kicked in, so we want to log out manually!
            navigator.logout(context)
        }
    }

    @Test
    fun basicLoginTest() {
        valueSetter.setLoginData("test@gmail.com", "password")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun disabledButtonsTest() {
        valueSetter.setLoginData("akjfamfgaksja@casf", "lajksfaj")
        onView(withId(R.id.register)).check(matches(isEnabled()))
        onView(withId(R.id.login)).check(matches(not(isEnabled())))
    }

    @Test
    fun failingLoginTest() {
        valueSetter.setLoginData("akjfamf@gaksja.casf", "lajksfaj")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.login)).check(matches(isDisplayed()))
    }

    @Test
    fun failingRegistrationTest() {
        valueSetter.setLoginData("newTest@gmail.com", "aosjkgaod")
        onView(withId(R.id.register)).perform(ViewActions.click())
        onView(withId(R.id.login)).check(matches(isDisplayed()))
    }

    @Test
    fun autoLoginTest() {
        valueSetter.setLoginData("test@gmail.com", "password")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun doubleAutoLoginTest() {
        //There has been a bug where AutoLogin failed if done multiple times in a row
        //This is the test to ensure the Bug doesn't resurface
        valueSetter.setLoginData("test@gmail.com", "password")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun autoLoginFailedTest() {
        valueSetter.setLoginData("test@gmail.com", "password")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        logOut()
        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }
}