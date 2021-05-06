package com.team06.focuswork.ui.util

import com.google.firebase.Timestamp
import java.util.*

object CalendarTimestampUtil {
    fun toCalendar(timestamp: Timestamp): Calendar {
        val result = Calendar.getInstance()
        result.time = timestamp.toDate()
        return result
    }

    fun toTimeStamp(calendar: Calendar): Timestamp {
        return Timestamp(calendar.time)
    }
}