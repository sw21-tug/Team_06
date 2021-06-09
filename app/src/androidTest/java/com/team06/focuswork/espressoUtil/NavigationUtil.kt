package com.team06.focuswork.espressoUtil

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.team06.focuswork.R

class NavigationUtil {
    fun logout() {
        onIdle()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }

    fun navigateToOverview() {
        onIdle()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_overview));
    }

    fun navigateToSettings() {
        onIdle()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    fun clickOnNewTaskButton() {
        onView(withId(R.id.fab)).perform(ViewActions.click())
        Thread.sleep(400)
        onView(withId(R.id.fragment_container_new_task)).check(matches(isDisplayed()))
    }

    fun chooseSetting(id: Int, text: String) {
        onIdle()
        navigateToSettings()
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(id)),
                    ViewActions.click()
                )
            )
        onView(withText(text)).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()))
            .perform(ViewActions.click())
    }
}