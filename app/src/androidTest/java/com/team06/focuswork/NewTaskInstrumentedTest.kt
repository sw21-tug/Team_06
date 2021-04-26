package com.team06.focuswork

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.TimestampProto
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class NewTaskInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        onView(withId(R.id.fab))
                .perform(click())
        // Wait short amount of time to ensure everything has loaded
        Thread.sleep(400)
        onView(withId(R.id.containerCreateTask))
            .check(matches(isDisplayed()))
    }
    //region UI Utility functions
    private fun setupTaskStrings(taskName: String, taskDescription: String) {
        onView(withId(R.id.taskName))
            .perform(clearText(), typeText(taskName))
        onView(withId(R.id.taskDescription))
            .perform(clearText(), typeText(taskDescription))
        onView(isRoot())
            .perform(closeSoftKeyboard())
    }
    private fun setStartDateValues(cal: Calendar) {
        onView(withId(R.id.taskStartDate))
            .perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)))
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
            .perform(PickerActions.setDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)))
        onView(withId(android.R.id.button1)).perform(click())
    }
    private fun setEndTimeValues(cal: Calendar) {

        onView(withId(R.id.taskEndTime))
            .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
        onView(withId(android.R.id.button1)).perform(click())
    }
    //endregion
    //region FirebaseFireStore Utility functions
    private fun getFirestoreDocRef() : CollectionReference{
        val db = FirebaseFirestore.getInstance()
        return db.collection("User")
            .document("dggkbNlMM7QqSWjj8Nii")
            .collection("Task")
    }
    private fun assertFirestoreContainsTask(taskName: String, taskDescription: String,
                                            startTime: Calendar, endTime: Calendar) {
        val docRef = getFirestoreDocRef()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Firebase get successful
                    Assert.assertEquals(taskName, document.documents[0]["name"])
                    Assert.assertEquals(taskDescription, document.documents[0]["description"])
                    val startTimestamp = document.documents[0]["startTime"] as Timestamp
                    val endTimestamp = document.documents[0]["endTime"] as Timestamp
                    Assert.assertEquals(startTime.time, startTimestamp.toDate())
                    Assert.assertEquals(endTime.time, endTimestamp.toDate())
                } else {
                    // Could not find document
                    Assert.assertTrue("Firestore could not find document Firestore", false)
                }
            }
            .addOnFailureListener { exception ->
                // Firebase get failed
                Assert.assertTrue("Firebase get failed", false)
            }
    }
    //endregion
    @Test
    fun createSimpleTask() {
        // At first, Task Create Button should not be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(not(isEnabled())))

        setupTaskStrings("createSimpleTask", "SimpleTaskDescription");
        val startDate = GregorianCalendar(2022, 10, 22, 10, 0)
        val endDate = GregorianCalendar(2022, 10, 22, 11, 0)
        setStartDateValues(startDate)
        setStartTimeValues(startDate)
        setEndDateValues(endDate)
        setEndTimeValues(endDate)

        // Task Create Button should now be enabled
        onView(withId(R.id.taskCreate))
                .check(matches(isEnabled()))
                .perform(click())

        // After click, overview should be shown again
        onView(withId(R.id.frame_layout_overview))
                .check(matches(isDisplayed()))

        /*assertFirestoreContainsTask(
            "createSimpleTask",
            "SimpleTaskDescription",
            startDate,
            endDate)*/
    }
    @Test
    fun namelessTask() {
        // At first, Task Create Button should not be enabled
        onView(withId(R.id.taskCreate))
            .check(matches(not(isEnabled())))

        setupTaskStrings("", "namelessTask description");
        val startDate = GregorianCalendar(2022, 10, 22, 10, 0)
        val endDate = GregorianCalendar(2022, 10, 22, 11, 0)
        setStartDateValues(startDate)
        setStartTimeValues(startDate)
        setEndDateValues(endDate)
        setEndTimeValues(endDate)

        // Task Create Button should still be disabled
        onView(withId(R.id.taskCreate))
            .check(matches(not(isEnabled())))
    }
}