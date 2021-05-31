package com.team06.focuswork

import android.view.Gravity
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.espressoUtil.MockUtil
import com.team06.focuswork.espressoUtil.NavigationUtil
import com.team06.focuswork.model.LoggedInUser
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
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
    private val user = LoggedInUser("SfuvPQ8Uf2wistKapXBQ")

    @Before
    fun init() {
        MockUtil.mockUser(user)
    }

    private fun navigateToTaskDetail() {
        navigator.navigateToOverview()

        onView(withId(R.id.fragment_container_overview)).check(matches(isDisplayed()))
        Thread.sleep(1000)

        try {
            onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

        } catch (e: NoMatchingViewException) {
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.DATE, 1)
            //Maybe there is just no task in the current week -> add one!
            addTask(startDate, endDate)
            Thread.sleep(1000)
            onView(withId(R.id.task_item))
                .check(matches(isDisplayed()))
                .perform(click())
        }

        onView(withId(R.id.fragment_container_taskdetails))
            .check(matches(isDisplayed()))
    }

    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName)).perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription)).perform(clearText(), typeText(taskDescription))
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    private fun setStartDateValues(cal: Calendar) {
        onView(withId(R.id.taskStartDate))
            .perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(
                PickerActions.setDate(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            )
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun setStartTimeValues(cal: Calendar) {
        onView(withId(R.id.taskStartTime))
            .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun setEndDateValues(cal: Calendar) {
        onView(withId(R.id.taskEndDate))
            .perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(
                PickerActions.setDate(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            )
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun setEndTimeValues(cal: Calendar) {
        onView(withId(R.id.taskEndTime))
            .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun addTask(startDate: Calendar, endDate: Calendar) {
        onView(withId(R.id.fab))
            .perform(click())
        // Wait short amount of time to ensure everything has loaded
        Thread.sleep(400)

        onView(withId(R.id.taskCreate))
            .check(matches(not(isEnabled())))

        setupTaskStrings("createSimpleTask", "SimpleTaskDescription");
        setStartDateValues(startDate)
        setStartTimeValues(startDate)
        setEndDateValues(endDate)
        setEndTimeValues(endDate)

        // Task Create Button should now be enabled
        onView(withId(R.id.taskCreate))
            .check(matches(ViewMatchers.isEnabled()))
            .perform(click())

        Thread.sleep(800)

        // After click, overview should be shown again
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }

    private fun navigateToSettings() {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    private fun navigateToOverview() {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview));
    }

    @Test
    fun testBackPressed() {
        navigateToTaskDetail()
        pressBack()

        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWeekView() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.overviewTimeFrame_title)), click()
                )
            )

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.overview_time_frame_entries)

        onView(withText(array[1]))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        navigateToOverview()
        Thread.sleep(400)
        onView(withId(R.id.text_view_week))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testMonthView() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.overviewTimeFrame_title)), click()
                )
            )

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.overview_time_frame_entries)

        onView(withText(array[2]))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.textview_month))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testDayView() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.overviewTimeFrame_title)), click()
                )
            )

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.overview_time_frame_entries)

        onView(withText(array[0]))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.text_view_day))
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }

    @Test
    fun deleteTest() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.HOUR, 1)
        Thread.sleep(400)

        addTask(startDate, endDate)

        onView(withTagValue(`is`("Task:0" as Any?))).perform(click())

        onView(withId(R.id.fragment_container_taskdetails))
            .check(matches(isDisplayed()))

        onView(withId(R.id.menu_detail_delete))
            .perform(click())

        onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun editTest() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.HOUR, 1)
        Thread.sleep(400)

        addTask(startDate, endDate)

        onView(withTagValue(`is`("Task:0" as Any?))).perform(click())

        onView(withId(R.id.fragment_container_taskdetails))
            .check(matches(isDisplayed()))

        onView(withId(R.id.menu_detail_edit))
            .perform(click())
        Thread.sleep(400)
        onView(withId(R.id.fragment_container_new_task))
            .check(matches(isDisplayed()))

        onView(withId(R.id.taskName))
            .check(matches(withText("createSimpleTask")))
            .perform(clearText(), typeText("editedSimpleTask"), closeSoftKeyboard())
        onView(withId(R.id.taskDescription))
            .check(matches(withText("SimpleTaskDescription")))

        onView(withId(R.id.taskCreate))
            .perform(click())
        Thread.sleep(400)
        onView(withId(R.id.fragment_container_taskdetails))
            .check(matches(isDisplayed()))
        onView(withId(R.id.title_taskdetails))
            .check(matches(withText("editedSimpleTask")))
        pressBack()
        Thread.sleep(400)
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAllView() {
        navigateToSettings()

        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.overviewTimeFrame_title)), click()
                )
            )

        val array = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getStringArray(R.array.overview_time_frame_entries)

        onView(withText(array[3]))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        navigateToOverview()
        Thread.sleep(400)

        onView(withId(R.id.text_view_all))
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }
}