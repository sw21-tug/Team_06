package com.team06.focuswork

import com.team06.focuswork.ui.util.FilterUtil
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FilterUtilUnitTest {
    @Test
    fun dayFilter_isCorrect() {
        val today = Calendar.getInstance()

        val beforeToday = Calendar.getInstance()
        beforeToday.add(Calendar.DATE, -2)
        val afterToday = Calendar.getInstance()
        afterToday.add(Calendar.DATE, 2)

        assertFalse(FilterUtil.filterForDay(today,  beforeToday, beforeToday))
        assertFalse(FilterUtil.filterForDay(today,  afterToday, afterToday))
        assertTrue(FilterUtil.filterForDay(today, today, today))
        assertTrue(FilterUtil.filterForDay(today, beforeToday, afterToday))
        assertTrue(FilterUtil.filterForDay(today, today, afterToday))
        assertTrue(FilterUtil.filterForDay(today, beforeToday, today))
    }

    @Test
    fun weekFilter_isCorrect() {
        val today = Calendar.getInstance()

        val lastWeek = Calendar.getInstance()
        lastWeek.add(Calendar.DATE, -8)
        val nextWeek = Calendar.getInstance()
        nextWeek.add(Calendar.DATE, 8)

        assertFalse(FilterUtil.filterForWeek(today,  lastWeek, lastWeek))
        assertFalse(FilterUtil.filterForWeek(today,  nextWeek, nextWeek))
        assertTrue(FilterUtil.filterForWeek(today, today, today))
        assertTrue(FilterUtil.filterForWeek(today, lastWeek, nextWeek))
        assertTrue(FilterUtil.filterForWeek(today, today, nextWeek))
        assertTrue(FilterUtil.filterForWeek(today, lastWeek, today))
    }

    @Test
    fun monthFilter_isCorrect() {
        val today = Calendar.getInstance()

        val lastMonth = Calendar.getInstance()
        lastMonth.add(Calendar.DATE, -32)
        val nextMonth = Calendar.getInstance()
        nextMonth.add(Calendar.DATE, 32)

        assertFalse(FilterUtil.filterForMonth(today,  lastMonth, lastMonth))
        assertFalse(FilterUtil.filterForMonth(today,  nextMonth, nextMonth))
        assertTrue(FilterUtil.filterForMonth(today, today, today))
        assertTrue(FilterUtil.filterForMonth(today, lastMonth, nextMonth))
        assertTrue(FilterUtil.filterForMonth(today, today, nextMonth))
        assertTrue(FilterUtil.filterForMonth(today, lastMonth, today))
    }

    @Test
    fun setMonday_isCorrect() {
        val cal = Calendar.getInstance()

        FilterUtil.setMonday(cal)
        assert(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY &&
            cal.timeInMillis <= Calendar.getInstance().timeInMillis)
    }
}