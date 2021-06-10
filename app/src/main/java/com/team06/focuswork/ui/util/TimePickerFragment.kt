package com.team06.focuswork.ui.util


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import java.util.*

class TimePickerFragment(
    private val calendar: MutableLiveData<Calendar>
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = arguments?.getInt("HOUR") ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = arguments?.getInt("MINUTE") ?: Calendar.getInstance().get(Calendar.MINUTE)
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.value?.set(Calendar.MINUTE, minute)
        calendar.postValue(calendar.value)
    }

    companion object {
        const val TAG: String = "TimePickerDialog"
    }
}