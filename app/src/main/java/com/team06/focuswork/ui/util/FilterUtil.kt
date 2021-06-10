package com.team06.focuswork.ui.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.team06.focuswork.R
import java.text.SimpleDateFormat
import java.util.*

object FilterUtil {
    //This is needed because setting the field Calendar.DAY_OF_WEEK has flimsy behaviour
    fun setMonday(cal: Calendar): Calendar {
        var delta = 0
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> delta = 0
            Calendar.TUESDAY -> delta = -1
            Calendar.WEDNESDAY -> delta = -2
            Calendar.THURSDAY -> delta = -3
            Calendar.FRIDAY -> delta = -4
            Calendar.SATURDAY -> delta = -5
            Calendar.SUNDAY -> delta = -6
        }

        cal.add(Calendar.DAY_OF_MONTH, delta)
        return cal
    }

    fun filterForDay(day: Calendar, start: Calendar, end: Calendar): Boolean {

        //We don't care about the year-to-day conversion being accurate
        //since we only care about what day comes first, meaning leaving 1 day unused
        //for most years is of no consequence
        val startDay = start.get(Calendar.DAY_OF_YEAR) + start.get(Calendar.YEAR) * 366
        val endDay = end.get(Calendar.DAY_OF_YEAR) + end.get(Calendar.YEAR) * 366
        val currentDay = day.get(Calendar.DAY_OF_YEAR) + day.get(Calendar.YEAR) * 366

        return currentDay in startDay..endDay
    }

    //week can just be any day within the week
    fun filterForWeek(week: Calendar, start: Calendar, end: Calendar): Boolean {
        setMonday(week)

        val startDay = start.get(Calendar.DAY_OF_YEAR) + start.get(Calendar.YEAR) * 366
        val endDay = end.get(Calendar.DAY_OF_YEAR) + end.get(Calendar.YEAR) * 366
        val currentDay = week.get(Calendar.DAY_OF_YEAR) + week.get(Calendar.YEAR) * 366

        return (currentDay in startDay..endDay) || (currentDay + 1 in startDay..endDay) ||
            (currentDay + 2 in startDay..endDay) || (currentDay + 3 in startDay..endDay) ||
            (currentDay + 4 in startDay..endDay) || (currentDay + 5 in startDay..endDay) ||
            (currentDay + 6 in startDay..endDay)
    }

    fun filterForMonth(month: Calendar, start: Calendar, end: Calendar): Boolean {
        //This is highly awkward, yet the best way to efficiently ensure that changing
        //DAY_OF_MONTH doesn't change MONTH itself (which it sometimes did in my tests)
        val m = month.get(Calendar.MONTH)
        month.set(Calendar.DAY_OF_MONTH, 1)
        month.set(Calendar.MONTH, m)

        val startDay = start.get(Calendar.DAY_OF_YEAR) +
            start.get(Calendar.YEAR) * 366
        val endDay = end.get(Calendar.DAY_OF_YEAR) +
            end.get(Calendar.YEAR) * 366
        val firstOfMonth = month.get(Calendar.DAY_OF_YEAR) +
            month.get(Calendar.YEAR) * 366

        month.set(Calendar.DAY_OF_MONTH, month.getActualMaximum(Calendar.DAY_OF_MONTH))
        month.set(Calendar.MONTH, m)

        val lastOfMonth = month.get(Calendar.DAY_OF_YEAR) +
            month.get(Calendar.YEAR) * 366

        return (startDay in firstOfMonth..lastOfMonth || endDay in firstOfMonth..lastOfMonth ||
            firstOfMonth in (startDay + 1)..endDay || lastOfMonth in startDay until endDay)
    }

    fun getMonthText(cal: Calendar, context: Context): SpannableString {

        val monthName = SimpleDateFormat("MMMM").format(cal.time)
        val text = SpannableString(
            String.format(
                "%s %s, %d", context.getString(R.string.day_tasks_for),
                monthName, cal.get(Calendar.YEAR)
            )
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD),
            context.getString(R.string.day_tasks_for).length + 1,
            monthName.length + context.getString(R.string.day_tasks_for).length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    fun getWeekText(calMon: Calendar, calSun: Calendar): SpannableString {
        setMonday(calMon)
        val monday = SimpleDateFormat("EEEE").format(calMon.time)

        setMonday(calSun)
        calSun.add(Calendar.DATE, 6)
        val sunday = SimpleDateFormat("EEEE").format(calSun.time)


        val text = SpannableString(
            String.format(
                "%s, %2d.%2d.  -  %s, %2d.%2d.",
                monday, calMon.get(Calendar.DAY_OF_MONTH), calMon.get(Calendar.MONTH) + 1,
                sunday, calSun.get(Calendar.DAY_OF_MONTH), calSun.get(Calendar.MONTH) + 1
            )
        )

        text.setSpan(
            StyleSpan(Typeface.BOLD), 0,
            monday.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD), monday.length + 13,
            monday.length + sunday.length + 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    fun getDayText(cal: Calendar, context: Context): SpannableString {
        val dayName = SimpleDateFormat("EEEE").format(cal.time)
        val text = SpannableString(
            String.format(
                "%s %s, %d.%d.", context.getString(R.string.day_tasks_for),
                dayName, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1
            )
        )
        text.setSpan(
            StyleSpan(Typeface.BOLD), context.getString(R.string.day_tasks_for).length + 1,
            dayName.length + context.getString(R.string.day_tasks_for).length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }
}