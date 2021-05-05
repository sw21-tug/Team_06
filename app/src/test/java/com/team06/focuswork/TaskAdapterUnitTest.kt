package com.team06.focuswork

import android.content.Context
import com.team06.focuswork.data.Task
import com.team06.focuswork.ui.daydetails.DayFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.util.*


class TaskAdapterUnitTest {
    @Test
    fun correctFilteringTest() {
        val list: MutableList<Task> = mutableListOf()

        val calNow = Calendar.getInstance()
        val calIn5Min = Calendar.getInstance()
        calIn5Min.add(Calendar.MINUTE, 5)
        val calNextDay = Calendar.getInstance()
        calNextDay.add(Calendar.HOUR, 24)
        val calNextDayAnd1Hour = Calendar.getInstance()
        calNextDayAnd1Hour.add(Calendar.HOUR, 1)
        val calPrevDay = Calendar.getInstance()
        calPrevDay.add(Calendar.HOUR, -25) //25 to make sure end is not today
        val calPrevDayPlus5Min = Calendar.getInstance()
        calPrevDayPlus5Min.add(Calendar.HOUR, -25)
        calPrevDayPlus5Min.add(Calendar.MINUTE, 5)

        val task1 = Task("Task 1", "Description of Task 1",
            calNow, calIn5Min)
        val task2 = Task("Task 2", "Description of Task 2",
            calNextDay, calNextDayAnd1Hour)
        val task3 = Task("Task 3", "Description of Task 3",
            calPrevDay, calPrevDayPlus5Min)
        val task4 = Task("Task 4", "Description of Task 4",
            calNow, calNextDay)
        val task5 = Task("Task 5", "Description of Task 5",
            calPrevDay, calNow)
        val task6 = Task("Task 6", "Description of Task 6",
            calPrevDay, calNextDay)

        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task4)
        list.add(task5)
        list.add(task6)

        val context: Context = Mockito.mock(Context::class.java)
        val adapter = DayFragment.TaskAdapter(context, 0, list)

        //Now ensure only correct tasks are shown
        val itemList = adapter.getItems()
        assertEquals(4, itemList.size)
        assertEquals(true, itemList.contains(task1))
        assertEquals(false, itemList.contains(task2))
        assertEquals(false, itemList.contains(task3))
        assertEquals(true, itemList.contains(task4))
        assertEquals(true, itemList.contains(task5))
        assertEquals(true, itemList.contains(task6))
    }
}