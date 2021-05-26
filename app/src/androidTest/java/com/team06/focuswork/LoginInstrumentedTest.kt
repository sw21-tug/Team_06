package com.team06.focuswork

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.firestore.FirebaseFirestore
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
    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @After
    fun removeAutoLogin(){
        Log.d("LoginTest", "Pruning Preferences of Autologin data!")
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .remove("PASS").apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .remove("USER").apply()
    }

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

    private fun clickRegister() {
        Espresso.onView(ViewMatchers.withId(R.id.register))
            .perform(ViewActions.click())
    }

    private fun deleteUser(username: String) {
        FirebaseFirestore.getInstance()
            .collection("User")
            .whereEqualTo("email", username)
            .get().addOnSuccessListener { document ->
                assert(document.documents.size < 2)
                if (document.documents.isEmpty()) {
                    return@addOnSuccessListener
                }
                FirebaseFirestore
                    .getInstance()
                    .collection("User")
                    .document(document.documents[0].id)
                    .delete()
            }
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
        Espresso.onView(ViewMatchers.withId(R.id.register))
            .check(matches(ViewMatchers.isEnabled()))
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

    @Test
    fun failingRegistrationTest() {
        setLoginData("newTest@gmail.com", "aosjkgaod")
        clickRegister()
        Espresso.onView(ViewMatchers.withId(R.id.login))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun autoLoginTest() {
        setLoginData("test@gmail.com", "password")
        clickLogin()
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(matches(ViewMatchers.isDisplayed()))

        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun autoLoginFailedTest() {
        setLoginData("test@gmail.com", "password")
        clickLogin()
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(matches(ViewMatchers.isDisplayed()))

        removeAutoLogin()
        Thread.sleep(400)

        activityRule.scenario.close()
        ActivityScenario.launch(LoginActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.username))
            .check(matches(ViewMatchers.isDisplayed()))
    }
}