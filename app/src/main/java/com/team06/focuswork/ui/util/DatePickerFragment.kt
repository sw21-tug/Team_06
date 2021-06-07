package com.team06.focuswork.ui.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.team06.focuswork.ui.tasks.NewTaskFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DatePicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment(
    private val calendar: MutableLiveData<Calendar>
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = arguments?.getInt("YEAR") ?: Calendar.getInstance().get(Calendar.YEAR)
        val month = arguments?.getInt("MONTH") ?: Calendar.getInstance().get(Calendar.MONTH)
        val day = arguments?.getInt("DAY") ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val minDate = arguments?.getLong("MIN_DATE") ?: System.currentTimeMillis() - 1000

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.datePicker.minDate = minDate
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.value?.set(year, month, dayOfMonth)
        calendar.postValue(calendar.value)
    }

    companion object {
        const val TAG = "DatePickerDialog"
    }
}