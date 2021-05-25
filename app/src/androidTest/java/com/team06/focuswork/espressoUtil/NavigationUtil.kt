package com.team06.focuswork.espressoUtil

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.team06.focuswork.R

class NavigationUtil {
    fun navigateToOverview() {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview));
    }

    fun navigateToSettings() {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    fun clickOnNewTaskButton() {
        onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        Thread.sleep(400)
        onView(ViewMatchers.withId(R.id.fragment_container_new_task))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}