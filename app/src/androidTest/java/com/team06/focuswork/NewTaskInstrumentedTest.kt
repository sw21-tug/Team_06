package com.team06.focuswork

import android.view.Gravity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.ui.tasks.NewTaskFragment
import org.junit.Assert
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
    fun navigateNewTaskFragment() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.            .perform(click())
            .perform(DrawerActions.open)
        onView(withId(R.id.nav_new_task))
            .perform(click())
    }

    @Test
    fun createSimpleTask() {
        onView(withId(R.id.taskName))
            .perform(ViewActions.typeTextIntoFocusedView("Test123"))
        onView(withId(R.id.taskDescription))
            .perform(ViewActions.typeTextIntoFocusedView("Test1234"))
        onView(withId(R.id.taskCreate))
            .check(ViewAssertion { view, _ -> view.isEnabled })
    }
}