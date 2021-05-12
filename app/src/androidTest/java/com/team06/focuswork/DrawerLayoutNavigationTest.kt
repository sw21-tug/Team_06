package com.team06.focuswork

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawerLayoutNavigationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private fun openLayoutDrawer() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()); // Open Drawer
    }

    @Test
    fun testDrawerLayoutNavigation() {
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))

        openLayoutDrawer()

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_new_task))
        onView(withId(R.id.fragment_container_new_task))
            .check(matches(isDisplayed()))
        Thread.sleep(200)

        openLayoutDrawer()

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_day_overview))
        onView(withId(R.id.fragment_container_day))
            .check(matches(isDisplayed()))
        Thread.sleep(200)

        openLayoutDrawer()

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_timer))
        onView(withId(R.id.fragment_container_timer))
            .check(matches(isDisplayed()))
        Thread.sleep(200)

        openLayoutDrawer()

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings))
        onView(withId(androidx.preference.R.id.recycler_view))
            .check(matches(isDisplayed()))
        Thread.sleep(200)

        openLayoutDrawer()

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview))
        onView(withId(R.id.fragment_container_overview))
            .check(matches(isDisplayed()))
    }

}