package com.team06.focuswork.ui.util


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DatePicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val liveDataLoc = MutableLiveData<Calendar>()
    val liveData: LiveData<Calendar> = liveDataLoc

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arg = arguments

        val cal = Calendar.getInstance()
        val hour = arg?.getInt("HOUR") ?: cal.get(Calendar.HOUR_OF_DAY)
        val minute = arg?.getInt("MINUTE") ?: cal.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    companion object {
        const val TAG = "TimePickerDialog"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        liveDataLoc.value = cal
    }
}