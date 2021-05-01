package com.team06.focuswork.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentNewTaskBinding
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import com.team06.focuswork.ui.util.DatePickerFragment
import com.team06.focuswork.ui.util.TimePickerFragment
import java.text.DateFormat.*
import java.util.*


class NewTaskFragment : Fragment() {

    private lateinit var workingTask: Task
    private val startDatePicker = DatePickerFragment(this, true)
    private val startTimePicker = TimePickerFragment(this, true)
    private val endDatePicker = DatePickerFragment(this, false)
    private val endTimePicker = TimePickerFragment(this, false)
    private lateinit var binding: FragmentNewTaskBinding
    var startCalendar: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    var endCalendar: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskNameView = binding.taskName
        val taskDescriptionView = binding.taskDescription

        taskNameView.doAfterTextChanged { checkTextFilled(view) }
        taskDescriptionView.doAfterTextChanged { checkTextFilled(view) }

        prepareStartDateTextView(binding.taskStartDate)
        prepareStartTimeTextView(binding.taskStartTime)
        endCalendar.value?.add(Calendar.HOUR, 1);
        prepareEndDateTextView(binding.taskEndDate)
        prepareEndTimeTextView(binding.taskEndTime)
        setupObserverCallbacks()

        binding.taskCreate.setOnClickListener {
            saveTask()
            findNavController().navigate(R.id.nav_overview)
        }
    }

    private fun saveTask() {
        val db = FirebaseFirestore.getInstance()
        val task: MutableMap<String, Any> = HashMap()
        task["name"] = binding.taskName.text.toString()
        task["description"] = binding.taskDescription.text.toString()
        task["startTime"] = CalendarTimestampUtil.toTimeStamp(startCalendar.value!!)
        task["endTime"] = CalendarTimestampUtil.toTimeStamp(endCalendar.value!!)
        db.collection("User")
                .document("dggkbNlMM7QqSWjj8Nii")
                .collection("Task")
                .add(task)
    }

    private fun prepareStartTimeTextView(startTimeTextView: TextView) {
        startTimeTextView.text = formatTime(startCalendar.value)
        startTimeTextView.setOnClickListener {
            startTimePicker.arguments = createDateOrTimeBundle(isDate = false, startBundle = true)
            startTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
        }
    }

    private fun prepareStartDateTextView(taskStartDate: TextView) {
        taskStartDate.text = formatDate(startCalendar.value)
        taskStartDate.setOnClickListener {
            startDatePicker.arguments = createDateOrTimeBundle(isDate = true, startBundle = true)
            startDatePicker.show(childFragmentManager, DatePickerFragment.TAG)
        }
    }

    private fun prepareEndTimeTextView(endTimeTextView: TextView) {
        endTimeTextView.text = formatTime(endCalendar.value)
        endTimeTextView.setOnClickListener {
            endTimePicker.arguments = createDateOrTimeBundle(isDate = false, startBundle = false)
            endTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
        }
    }

    private fun prepareEndDateTextView(endDateTextView: TextView) {
        endDateTextView.text = formatDate(endCalendar.value)
        endDateTextView.setOnClickListener {
            endDatePicker.arguments = createDateOrTimeBundle(isDate = true, startBundle = false)
            endDatePicker.show(childFragmentManager, DatePickerFragment.TAG)

        }
    }

    private fun setupObserverCallbacks() {
        startCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val cal = it ?: return@Observer
            val nowCal = Calendar.getInstance()
            nowCal.add(Calendar.MINUTE, -1)
            if (cal.before(nowCal)) {
                cal.add(Calendar.DAY_OF_YEAR, 1)
                startCalendar.value = cal
            }
            if (endCalendar.value?.before(cal)!!) {
                val newEndCal = GregorianCalendar(
                        startCalendar.value?.get(Calendar.YEAR)!!,
                        startCalendar.value?.get(Calendar.MONTH)!!,
                        startCalendar.value?.get(Calendar.DAY_OF_MONTH)!!,
                        endCalendar.value?.get(Calendar.HOUR_OF_DAY)!!,
                        endCalendar.value?.get(Calendar.MINUTE)!!)
                endCalendar.value = newEndCal
            }
            binding.taskStartDate.text = formatDate(cal)
            binding.taskStartTime.text = formatTime(cal)
        })
        endCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val cal = it ?: return@Observer
            if (cal.before(startCalendar.value)) {
                val newEndCal = GregorianCalendar(
                        startCalendar.value?.get(Calendar.YEAR)!!,
                        startCalendar.value?.get(Calendar.MONTH)!!,
                        startCalendar.value?.get(Calendar.DAY_OF_MONTH)!!,
                        endCalendar.value?.get(Calendar.HOUR_OF_DAY)!!,
                        endCalendar.value?.get(Calendar.MINUTE)!!)
                endCalendar.value = newEndCal
            }
            binding.taskEndDate.text = formatDate(cal)
            binding.taskEndTime.text = formatTime(cal)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context?.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun formatDate(cal: Calendar?): String {
        return getDateInstance(MEDIUM).format(cal!!.time)
    }

    private fun formatTime(cal: Calendar?): String {
        return getTimeInstance(SHORT).format(cal!!.time)
    }

    private fun checkTextFilled(view: View) {
        view.findViewById<Button>(R.id.taskCreate).isEnabled =
                !(view.findViewById<TextView>(R.id.taskName).text.isBlank() ||
                        view.findViewById<TextView>(R.id.taskDescription).text.isBlank())
    }

    private fun createDateOrTimeBundle(isDate: Boolean, startBundle: Boolean): Bundle {
        val cal = if (startBundle) startCalendar.value else endCalendar.value
        return if (isDate) bundleOf(
                Pair("YEAR", cal?.get(Calendar.YEAR)),
                Pair("MONTH", cal?.get(Calendar.MONTH)),
                Pair("DAY", cal?.get(Calendar.DAY_OF_MONTH)),
                Pair("MIN_DATE",
                        if (startBundle) System.currentTimeMillis() - 1000
                        else startCalendar.value?.timeInMillis
                )
        ) else bundleOf(
                Pair("HOUR", cal?.get(Calendar.HOUR_OF_DAY)),
                Pair("MINUTE", cal?.get(Calendar.MINUTE))
        )
    }
}