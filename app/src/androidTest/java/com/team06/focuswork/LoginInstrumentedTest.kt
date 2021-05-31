package com.team06.focuswork

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.ui.login.LoginActivity
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    private var context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val valueSetter = PrepareValuesUtil()

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @After
    fun removeAutoLogin() {
        Log.d("LoginTest", "Pruning Preferences of Autologin data!")
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("PASS").apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("USER").apply()
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
    fun autoLoginFailedTest() {
        valueSetter.setLoginData("test@gmail.com", "password")
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        removeAutoLogin()
        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }
}