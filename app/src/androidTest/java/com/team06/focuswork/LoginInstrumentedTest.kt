package com.team06.focuswork

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.ui.login.LoginActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
            ActivityScenarioRule(LoginActivity::class.java)

    private fun setLoginData(username: String, password: String) {
        Espresso.onView(ViewMatchers.withId(R.id.username))
                .perform(ViewActions.clearText(), ViewActions.typeText(username))
        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.clearText(), ViewActions.typeText(password))
        Espresso.onView(ViewMatchers.isRoot())
                .perform(ViewActions.closeSoftKeyboard())
    }

    private fun clickLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.login))
                .perform(ViewActions.click())
    }
    @Test
    fun basicLoginTest() {
        setLoginData("test@gmail.com", "password")
        clickLogin()
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun disabledButtonsTest() {
        setLoginData("akjfamfgaksja@casf", "lajksfaj")
        Espresso.onView(ViewMatchers.withId(R.id.login))
                .check(matches(not(ViewMatchers.isEnabled())))
    }

    @Test
    fun failingLoginTest() {
        setLoginData("akjfamf@gaksja.casf", "lajksfaj")
        clickLogin()
        Espresso.onView(ViewMatchers.withId(R.id.login))
                .check(matches(ViewMatchers.isDisplayed()))
    }

}