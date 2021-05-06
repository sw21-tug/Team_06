package com.team06.focuswork

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.model.LoggedInUser
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverviewDetailsInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val user = LoggedInUser("SfuvPQ8Uf2wistKapXBQ")

    @Before
    fun init() {
        // Mock Test User
        mockkObject(LoginRepository)
        every { LoginRepository.getUser() } answers { user }
    }

    private fun navigateToTaskDetail() {
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview))

        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(withId(R.id.task_item))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.fragment_container_taskdetails))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testBackPressed() {
        navigateToTaskDetail()
        pressBack()

        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }
}