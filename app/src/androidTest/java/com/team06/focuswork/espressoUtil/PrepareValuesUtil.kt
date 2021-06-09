package com.team06.focuswork.espressoUtil

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.team06.focuswork.R
import org.hamcrest.Matchers
import java.util.*

class PrepareValuesUtil {

    fun setDateValues(id: Int, cal: Calendar) {
        onView(withId(id)).perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun setTimeValues(id: Int, cal: Calendar) {
        onView(withId(id)).perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
        onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun setLoginData(username: String, password: String) {
        onView(withId(R.id.username)).perform(clearText(), typeText(username))
        onView(withId(R.id.password)).perform(clearText(), typeText(password))
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    fun setRegisterData(firstname: String, lastname: String, username: String, password: String) {
        onView(withId(R.id.firstname)).perform(typeText(firstname))
        onView(withId(R.id.lastname)).perform(clearText(), typeText(lastname))
        onView(withId(R.id.username)).perform(clearText(), typeText(username))
        onView(withId(R.id.password)).perform(clearText(), typeText(password))
        onView(isRoot()).perform(closeSoftKeyboard())
    }
}