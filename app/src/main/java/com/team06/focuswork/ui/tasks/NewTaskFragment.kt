package com.team06.focuswork.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentNewTaskBinding
import com.team06.focuswork.ui.util.DatePickerFragment
import com.team06.focuswork.ui.util.TimePickerFragment
import java.text.DateFormat.*
import java.util.*


class NewTaskFragment : Fragment() {

    private lateinit var workingTask: Task
    private val startDatePicker = DatePickerFragment()
    private val startTimePicker = TimePickerFragment()
    private val endDatePicker = DatePickerFragment()
    private val endTimePicker = TimePickerFragment()
    private val startCal = Calendar.getInstance()
    private val endCal = Calendar.getInstance()
    private lateinit var binding: FragmentNewTaskBinding

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
        endCal.add(Calendar.HOUR, 1)
        prepareEndDateTextView(binding.taskEndDate)
        prepareEndTimeTextView(binding.taskEndTime)

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
        task["startTime"] = Timestamp(Date(startCal.timeInMillis))
        task["endTime"] = Timestamp(Date(endCal.timeInMillis))
        db.collection("User")
            .document("dggkbNlMM7QqSWjj8Nii")
            .collection("Task")
            .add(task)
    }

    private fun prepareStartDateTextView(taskStartDate: TextView) {
        taskStartDate.text = formatDate(startCal)
        taskStartDate.setOnClickListener {
            startDatePicker.arguments = createDateOrTimeBundle(true)
            val calBeforeDateDialog = Calendar.getInstance()
            startDatePicker.show(childFragmentManager, DatePickerFragment.TAG)
            startDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if (cal < calBeforeDateDialog) {
                    showToast(getString(R.string.task_illegal_start_date_toast))
                } else {
                    startCal.set(
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    taskStartDate.text = formatDate(cal)
                }
            })
        }
    }

    private fun prepareEndTimeTextView(endTimeTextView: TextView) {
        endTimeTextView.text = formatTime(endCal)
        endTimeTextView.setOnClickListener {
            endTimePicker.arguments = createDateOrTimeBundle(false)
            endTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
            endTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if (cal < startCal) {
                    showToast(getString(R.string.task_illegal_end_time_toast))
                } else {
                    endCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                    endCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                    endTimeTextView.text = formatTime(cal)
                }

            })
        }
    }

    private fun prepareEndDateTextView(endDateTextView: TextView) {
        endDateTextView.text = formatDate(endCal)
        endDateTextView.setOnClickListener {
            endDatePicker.arguments = createDateOrTimeBundle(true)
            endDatePicker.show(childFragmentManager, DatePickerFragment.TAG)
            endDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if (cal < startCal) {
                    showToast(getString(R.string.task_illegal_end_date_toast))
                } else {
                    endCal.set(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                    )
                    endDateTextView.text = formatDate(cal)
                }
            })
        }
    }

    private fun prepareStartTimeTextView(startTimeTextView: TextView) {
        startTimeTextView.text = formatTime(startCal)
        startTimeTextView.setOnClickListener {
            startTimePicker.arguments = createDateOrTimeBundle(false)
            val calBeforeTimeDialog = Calendar.getInstance()
            startTimePicker.show(childFragmentManager, TimePickerFragment.TAG)
            startTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if (cal < calBeforeTimeDialog) {
                    showToast(getString(R.string.task_illegal_start_time_toast))
                } else {
                    startCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                    startCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                    startTimeTextView.text = formatTime(cal)
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context?.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun formatDate(cal: Calendar): String {
        return getDateInstance(MEDIUM).format(cal.time)
    }

    private fun formatTime(cal: Calendar): String {
        return getTimeInstance(SHORT).format(cal.time)
    }

    private fun checkTextFilled(view: View) {
        view.findViewById<Button>(R.id.taskCreate).isEnabled =
            !(view.findViewById<TextView>(R.id.taskName).text.isBlank() ||
                    view.findViewById<TextView>(R.id.taskDescription).text.isBlank())
    }

    private fun createDateOrTimeBundle(isDate: Boolean) = if (isDate) bundleOf(
        Pair("YEAR", endCal.get(Calendar.YEAR)),
        Pair("MONTH", endCal.get(Calendar.MONTH)),
        Pair("DAY", endCal.get(Calendar.DAY_OF_MONTH))
    ) else bundleOf(
        Pair("HOUR", endCal.get(Calendar.HOUR_OF_DAY)),
        Pair("MINUTE", endCal.get(Calendar.MINUTE))
    )
}