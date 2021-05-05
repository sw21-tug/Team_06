package com.team06.focuswork

import androidx.test.espresso.Espresso
import androidx.test.espresso.EspressoException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.ui.login.LoginActivity
import com.team06.focuswork.ui.login.RegisterActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<RegisterActivity> =
        ActivityScenarioRule(RegisterActivity::class.java)

    private fun setRegisterData(firstname: String, lastname: String, username: String, password: String) {
        Espresso.onView(ViewMatchers.withId(R.id.firstname))
            .perform(ViewActions.clearText(), ViewActions.typeText(firstname))
        Espresso.onView(ViewMatchers.withId(R.id.lastname))
            .perform(ViewActions.clearText(), ViewActions.typeText(lastname))
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(ViewActions.clearText(), ViewActions.typeText(username))
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.clearText(), ViewActions.typeText(password))
        Espresso.onView(ViewMatchers.isRoot())
            .perform(ViewActions.closeSoftKeyboard())
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
    fun basicRegistrationTest() {
        deleteUser("newTest@gmail.com")
        clickRegister()
        setRegisterData("Test", "Test2","newTest@gmail.com", "aosjkgaod")
        clickRegister()
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun disabledButtonsTest() {
        setRegisterData("asdfa", "asdfafasd","akjfamfgaksja@casf", "lajksfaj")
        Espresso.onView(ViewMatchers.withId(R.id.register))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun failingRegistrationTest() {
        clickRegister()
        setRegisterData("Max", "Mustermann","newTest@gmail.com", "aosjkgaod")
        clickRegister()
        Espresso.onView(ViewMatchers.withId(R.id.register))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}