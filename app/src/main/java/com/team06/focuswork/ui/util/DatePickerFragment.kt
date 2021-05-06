package com.team06.focuswork.ui.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.team06.focuswork.ui.tasks.NewTaskFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DatePicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment(private var newTaskFragment: NewTaskFragment, private val startPicker: Boolean) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arg = arguments
        val cal = Calendar.getInstance()
        val year = arg?.getInt("YEAR") ?: cal.get(Calendar.YEAR)
        val month = arg?.getInt("MONTH") ?: cal.get(Calendar.MONTH)
        val day = arg?.getInt("DAY") ?: cal.get(Calendar.DAY_OF_MONTH)
        val minDate = arg?.getLong("MIN_DATE") ?: System.currentTimeMillis() - 1000

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.datePicker.minDate = minDate
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (startPicker) {
            val cal = newTaskFragment.startCalendar.value
            cal?.set(year, month, dayOfMonth)
            newTaskFragment.startCalendar.value = cal
        } else {
            val cal = newTaskFragment.startCalendar.value
            cal?.set(year, month, dayOfMonth)
            newTaskFragment.endCalendar.value = cal
        }
    }

    companion object {
        const val TAG = "DatePickerDialog"
    }
}