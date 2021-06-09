package com.team06.focuswork

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.FireStoreCleanUp
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.ui.login.RegisterActivity
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentedTest {
    private var context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val valueSetter = PrepareValuesUtil()

    @After
    fun removeAutoLogin(){
        Log.d("LoginTest", "Pruning Preferences of Autologin data!")
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("PASS").apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("USER").apply()
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<RegisterActivity> =
        ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun basicRegistrationTest() {
        FireStoreCleanUp.deleteUser("newTest@gmail.com")
        valueSetter.setRegisterData("Test", "Test2", "newTest@gmail.com", "aosjkgaod")
        onView(withId(R.id.register)).perform(click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun disabledButtonsTest() {
        valueSetter.setRegisterData("asdfa", "asdfafasd", "akjfamfgaksja@casf", "lajksfaj")
        onView(withId(R.id.register)).check(matches(Matchers.not(isEnabled())))
    }

    @Test
    fun failingRegistrationTest() {
        valueSetter.setRegisterData("Max", "Mustermann", "newTest@gmail.com", "aosjkgaod")
        onView(withId(R.id.register)).perform(click())
        onView(withId(R.id.container_register_activity)).check(matches(isDisplayed()))
    }
}