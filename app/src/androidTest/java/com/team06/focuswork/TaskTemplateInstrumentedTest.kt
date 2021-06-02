package com.team06.focuswork

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.model.LoggedInUser
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskTemplateInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val navigator = NavigationUtil()
    private val user = LoggedInUser("dggkbNlMM7QqSWjj8Nii")
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val taskName = "TestTaskname"
    private val taskDescription = "TestTaskDescription"
    private val templateName = "TestTemplate"

    @Before
    fun init() {
        MockUtil.mockUser(user)
        navigator.clickOnNewTaskButton()
    }

    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName)).perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription)).perform(clearText(), typeText(taskDescription))
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    private fun createTemplate(taskName: String, taskDescription: String) {
        setupTaskStrings(taskName, taskDescription)

        // Save as template
        onView(withId(R.id.taskSaveTemplate)).perform(click())
        onView(withId(R.id.templateTitle)).perform(clearText(), typeText(templateName))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(android.R.id.button1)).perform(click())

        // Clear task fields
        onView(withId(R.id.taskName)).perform(clearText())
        onView(withId(R.id.taskDescription)).perform(clearText())
    }

    private fun loadTemplate() {
        openActionBarOverflowOrOptionsMenu(targetContext)
        onView(withText(R.string.menu_item_load_template)).perform(click())
        onView(withText(templateName)).perform(click())
    }

    private fun deleteTemplate() {
        openActionBarOverflowOrOptionsMenu(targetContext)
        onView(withText(R.string.menu_item_delete_template)).perform(click())
        onView(withText(templateName)).perform(click())
    }

    @Test
    fun createTaskTemplateTest() {
        createTemplate(taskName, taskDescription)
        loadTemplate()

        // Assert template has been loaded
        onView(withId(R.id.taskName)).check(matches(withText(taskName)))
        onView(withId(R.id.taskDescription)).check(matches(withText(taskDescription)))

        deleteTemplate()

        //Assert template was deleted
        openActionBarOverflowOrOptionsMenu(targetContext)
        onView(withText(R.string.menu_item_load_template))
            .check(matches(not(hasDescendant(withText(templateName)))))
    }

    @Test
    fun noDuplicateTemplateTest() {
        createTemplate(taskName, taskDescription)
        loadTemplate()
        createTemplate(taskName, taskDescription)
        loadTemplate()

        // Assert template has been loaded
        onView(withId(R.id.taskName)).check(matches(withText(taskName)))
        onView(withId(R.id.taskDescription)).check(matches(withText(taskDescription)))

        deleteTemplate()

        //Assert template was deleted
        openActionBarOverflowOrOptionsMenu(targetContext)
        onView(withText(R.string.menu_item_load_template))
            .check(matches(not(hasDescendant(withText(templateName)))))
    }
}