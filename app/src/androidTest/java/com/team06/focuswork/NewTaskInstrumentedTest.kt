package com.team06.focuswork

import android.app.Application
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.content.pm.ApplicationInfoBuilder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team06.focuswork.ui.tasks.NewTaskFragment
import org.hamcrest.CoreMatchers.not
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
        launchFragmentInContainer<NewTaskFragment>()
    }

    @Test
    fun createSimpleTask() {
        // At first, Task Create Button should not be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(not(isEnabled())))

        // Set name and description
        onView(withId(R.id.taskName))
                .perform(typeText("TaskName"))
        onView(withId(R.id.taskDescription))
                .perform(typeText("Task Description"))

        // Set Start and End
        /*
        //TODO check dialog that opens
        onView(withId(R.id.taskStartDate))
           .perform(click())
        onView(withId(R.id.date))
        onView(withId() )
                //PickerActions.setDate(2021, 6, 1))
        onView(withId(R.id.taskStartTime))
                .perform(PickerActions.setTime(8, 30))
        onView(withId(R.id.taskEndDate))
                .perform(PickerActions.setDate(2021, 6, 1))
        onView(withId(R.id.taskEndTime))
                .perform(PickerActions.setTime(13, 45))
        */

        onView(isRoot())
                .perform(closeSoftKeyboard())

        // Task Create Button should now be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(isEnabled()))
                .perform(click())


        // After click, overview should be shown again
        onView(withId(R.id.nav_new_task))
                .check(matches(not(isDisplayed())))
        onView(withId(R.id.nav_overview))
                .check(matches(isDisplayed()))

    }
}