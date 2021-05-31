package com.team06.focuswork

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.FireStoreCleanUp
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.model.LoggedInUser
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class NewTaskInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val navigator = NavigationUtil()
    private val valueSetter = PrepareValuesUtil()
    private val startDate = GregorianCalendar(2022, 10, 22, 10, 0)
    private val endDate = GregorianCalendar(2022, 10, 22, 11, 0)
    private val startDateSecondTask = GregorianCalendar(2022, 10, 23, 15, 15)
    private val endDateSecondTask = GregorianCalendar(2022, 10, 23, 16, 30)
    private val user = LoggedInUser("dggkbNlMM7QqSWjj8Nii")

    @Before
    fun init() {
        MockUtil.mockUser(user)
        val view = context.resources.getStringArray(R.array.overview_time_frame_entries).last()
        navigator.chooseSetting(R.string.overviewTimeFrame_title, view)
        navigator.navigateToOverview()
        navigator.clickOnNewTaskButton()
    }

    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName)).perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription)).perform(clearText(), typeText(taskDescription))
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    private fun setTimeValues(startDate: Calendar, endDate: Calendar) {
        valueSetter.setDateValues(R.id.taskStartDate, startDate)
        valueSetter.setTimeValues(R.id.taskStartTime, startDate)
        valueSetter.setDateValues(R.id.taskEndDate, endDate)
        valueSetter.setTimeValues(R.id.taskEndTime, endDate)
    }

    //endregion
    @Test
    fun createSimpleTaskTest() {
        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))
        setupTaskStrings("createSimpleTask", "SimpleTaskDescription");
        setTimeValues(startDate, endDate)

        onView(withId(R.id.taskCreate)).check(matches(isEnabled())).perform(click())
        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.task_item_title), withText("createSimpleTask")))
            .check(matches(isDisplayed()))

        FireStoreCleanUp.deleteAllTasksOfCurrentUser()
    }

    @Test
    fun createTaskWithoutNameTest() {
        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))
        setupTaskStrings("", "namelessTask description")
        setTimeValues(startDate, endDate)
        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))
    }

    @Test
    fun createTaskWithoutDescriptionTest() {
        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))
        setupTaskStrings("TaskName no Desc", "");
        setTimeValues(startDate, endDate)
        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))
    }

    @Test
    fun createTwoTasksTest() {
        val taskName = arrayOf("myFirstTask", "My Second Task")
        val taskDescription = arrayOf("First Task Description", "Second task Desc.")
        val startDates = arrayOf(startDate, startDateSecondTask)
        val endDates = arrayOf(endDate, endDateSecondTask)

        for (i in taskName.indices) {
            setupTaskStrings(taskName[i], taskDescription[i])
            setTimeValues(startDates[i], endDates[i])
            onView(withId(R.id.taskCreate)).check(matches(isEnabled())).perform(click())

            onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
            navigator.clickOnNewTaskButton()
        }

        navigator.navigateToOverview()
        onView(allOf(withId(R.id.task_item_title), withText("myFirstTask")))
            .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.task_item_title), withText("My Second Task")))
            .check(matches(isDisplayed()))

        FireStoreCleanUp.deleteAllTasksOfCurrentUser()
    }
}