package com.team06.focuswork

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.espressoUtil.PrepareValuesUtil
import com.team06.focuswork.model.LoggedInUser
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class OverviewDetailsInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val navigator = NavigationUtil()
    private val valueSetter = PrepareValuesUtil()
    private val user = LoggedInUser("SfuvPQ8Uf2wistKapXBQ")
    private val views = InstrumentationRegistry.getInstrumentation()
        .targetContext.resources.getStringArray(R.array.overview_time_frame_entries)
    private lateinit var startDate : Calendar
    private lateinit var endDate : Calendar

    @Before
    fun init() {
        MockUtil.mockUser(user)
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        endDate.add(Calendar.HOUR, 1)
        startDate.add(Calendar.MONTH, 1)
        endDate.add(Calendar.MONTH, 1)
    }

    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName)).perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription)).perform(clearText(), typeText(taskDescription))
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    private fun addTask(startDate: Calendar, endDate: Calendar) {
        onView(withId(R.id.fab)).perform(click())
        Thread.sleep(400)

        onView(withId(R.id.taskCreate)).check(matches(not(isEnabled())))

        setupTaskStrings("createSimpleTask", "SimpleTaskDescription")
        valueSetter.setDateValues(R.id.taskStartDate, startDate)
        valueSetter.setTimeValues(R.id.taskStartTime, startDate)
        valueSetter.setDateValues(R.id.taskEndDate, endDate)
        valueSetter.setTimeValues(R.id.taskEndTime, endDate)

        onView(withId(R.id.taskCreate)).check(matches(isEnabled())).perform(click())
        Thread.sleep(800)

        // After click, overview should be shown again
        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
    }

    @Test
    fun testDayView() {
        navigator.chooseSetting(R.string.overviewTimeFrame_title, views[0])
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.text_view_day)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
    }

    @Test
    fun testWeekView() {
        navigator.chooseSetting(R.string.overviewTimeFrame_title, views[1])
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.text_view_week)).check(matches(isDisplayed()))
    }

    @Test
    fun testMonthView() {
        navigator.chooseSetting(R.string.overviewTimeFrame_title, views[2])
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.textview_month)).check(matches(isDisplayed()))
    }

    @Test
    fun testAllView() {
        navigator.chooseSetting(R.string.overviewTimeFrame_title, views[3])
        navigator.navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.text_view_all)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteTest() {
        addTask(startDate, endDate)
        onView(withTagValue(`is`("Task:0" as Any?))).perform(click())
        onView(withId(R.id.fragment_container_taskdetails)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_detail_delete)).perform(click())
        onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog()).check(matches(isDisplayed())).perform(click())
    }

    @Test
    fun editTest() {
        addTask(startDate, endDate)
        onView(withTagValue(`is`("Task:0" as Any?))).perform(click())
        onView(withId(R.id.fragment_container_taskdetails)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_detail_edit)).perform(click())
        Thread.sleep(400)

        onView(withId(R.id.fragment_container_new_task)).check(matches(isDisplayed()))
        onView(withId(R.id.taskName)).check(matches(withText("createSimpleTask")))
            .perform(clearText(), typeText("editedSimpleTask"), closeSoftKeyboard())
        onView(withId(R.id.taskDescription)).check(matches(withText("SimpleTaskDescription")))
        onView(withId(R.id.taskCreate)).perform(click())
        Thread.sleep(400)

        onView(withId(R.id.fragment_container_taskdetails)).check(matches(isDisplayed()))
        onView(withId(R.id.title_taskdetails)).check(matches(withText("editedSimpleTask")))
        pressBack()
        Thread.sleep(400)

        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
    }
}