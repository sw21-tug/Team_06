package com.team06.focuswork

import com.google.firebase.Timestamp
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class CalendarTimestampUtilTest {

    @Test
    fun calendarToTimestampTest() {
        val stamp = Timestamp(Date())
        val cal = CalendarTimestampUtil.toCalendar(stamp)
        assertEquals(cal.time, stamp.toDate())
    }

    @Test
    fun timestampToCalenderTest() {
        val cal = Calendar.getInstance()
        val stamp = CalendarTimestampUtil.toTimeStamp(cal)
        assertEquals(cal.time, stamp.toDate())
    }
}