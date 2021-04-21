package com.team06.focuswork

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewTaskInstrumentedTest {
    @Test
    fun createSimpleTask() {
        onView(withId(R.id.taskName))
                .perform(ViewActions.typeTextIntoFocusedView("Test123"))
        onView(withId(R.id.taskDescription))
                .perform(ViewActions.typeTextIntoFocusedView("Test1234"))

    }
}