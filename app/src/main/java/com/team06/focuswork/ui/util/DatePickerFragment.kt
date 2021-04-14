package com.team06.focuswork.ui.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team06.focuswork.R
import java.time.LocalDateTime
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DatePicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val liveDataLoc = MutableLiveData<Calendar>()
    val liveData: LiveData<Calendar> = liveDataLoc

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Fetch current Day/month/year
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)
        liveDataLoc.value = cal
    }

    companion object {
        const val TAG = "DatePickerDialog"
    }
}