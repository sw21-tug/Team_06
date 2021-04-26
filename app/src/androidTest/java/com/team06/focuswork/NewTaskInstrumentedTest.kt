package com.team06.focuswork

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NewTaskInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        /*onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_new_task))
*/
        onView(withId(R.id.fab))
                .perform(click())
        // Wait short amount of time to ensure everything has loaded
        Thread.sleep(400)
        onView(withId(R.id.containerCreateTask))
            .check(matches(isDisplayed()))
    }
    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName))
            .perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription))
            .perform(clearText(), typeText(taskDescription))
        onView(isRoot())
            .perform(closeSoftKeyboard())
    }
    private fun setStartDateValues(year: Int, month: Int, day: Int) {
        onView(withId(R.id.taskStartDate))
            .perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, month, day))
        onView(withId(android.R.id.button1)).perform(click())
    }
    private fun setStartTimeValues(hour: Int, minute: Int) {
        onView(withId(R.id.taskStartTime))
            .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(hour, minute))
        onView(withId(android.R.id.button1)).perform(click())
    }
    private fun setEndDateValues(year: Int, month: Int, day: Int) {
        onView(withId(R.id.taskEndDate))
            .perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, month, day))
        onView(withId(android.R.id.button1)).perform(click())
    }
    private fun setEndTimeValues(hour: Int, minute: Int) {
        onView(withId(R.id.taskEndTime))
            .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(hour, minute))
        onView(withId(android.R.id.button1)).perform(click())
    }
    @Test
    fun createSimpleTask() {
        // At first, Task Create Button should not be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(not(isEnabled())))

        setupTaskStrings("createSimpleTask", "SimpleTaskDescription");
        setStartDateValues(2022, 10, 22)
        setStartTimeValues(10, 0)
        setEndDateValues(2022, 10, 22)
        setEndTimeValues(11, 0)

        // Task Create Button should now be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(isEnabled()))
                .perform(click())

        // After click, overview should be shown again
        onView(withId(R.id.frame_layout_overview))
                .check(matches(isDisplayed()))
    }
    @Test
    fun namelessTask() {
        // At first, Task Create Button should not be enabled
        onView(withId(R.id.taskCreate))
            .check(matches(not(isEnabled())))

        setupTaskStrings("", "namelessTask description");
        setStartDateValues(2022, 10, 22)
        setStartTimeValues(10, 0)
        setEndDateValues(2022, 10, 22)
        setEndTimeValues(9, 0)

        // Task Create Button should still be disabled
        onView(withId(R.id.taskCreate))
            .check(matches(not(isEnabled())))
    }
}