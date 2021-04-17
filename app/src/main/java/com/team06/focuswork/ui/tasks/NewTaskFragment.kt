package com.team06.focuswork.ui.tasks

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.ui.util.DatePickerFragment
import com.team06.focuswork.ui.util.TimePickerFragment
import java.text.DateFormat
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskNameView = view.findViewById<EditText>(R.id.taskName)
        val taskDescriptionView = view.findViewById<EditText>(R.id.taskDescription)

        taskNameView.doAfterTextChanged {
            checkTextFilled(view)
        }
        taskDescriptionView.doAfterTextChanged {
            checkTextFilled(view)
        }

        val startDateTextView = view.findViewById<TextView>(R.id.taskStartDate)
        val startTimeTextView = view.findViewById<TextView>(R.id.taskStartTime)
        val endDateTextView = view.findViewById<TextView>(R.id.taskEndDate)
        val endTimeTextView = view.findViewById<TextView>(R.id.taskEndTime)
        startDateTextView.text = formatDate(startCal)
        startTimeTextView.text = formatTime(startCal)
        endCal.add(Calendar.HOUR, 1)
        endDateTextView.text = formatDate(endCal)
        endTimeTextView.text = formatTime(endCal)

        startDateTextView.setOnClickListener{

            val bundle = Bundle()
            bundle.putInt("YEAR", endCal.get(Calendar.YEAR))
            bundle.putInt("MONTH", endCal.get(Calendar.MONTH))
            bundle.putInt("DAY", endCal.get(Calendar.DAY_OF_MONTH))
            endDatePicker.arguments = bundle

            val calBeforeDateDialog = Calendar.getInstance()
            startDatePicker.show(
                    childFragmentManager, DatePickerFragment.TAG
            )
            startDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if(cal < calBeforeDateDialog)
                {
                    Toast.makeText(context?.applicationContext,
                            getString(R.string.task_illegal_start_date_toast),
                            Toast.LENGTH_LONG).show()
                }
                else
                {
                    startCal.set(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH))
                    startDateTextView.text = formatDate(cal)
                }
            })
        }

        startTimeTextView.setOnClickListener{

            val bundle = Bundle()
            bundle.putInt("HOUR", endCal.get(Calendar.HOUR_OF_DAY))
            bundle.putInt("MINUTE", endCal.get(Calendar.MINUTE))
            startTimePicker.arguments = bundle

            val calBeforeTimeDialog = Calendar.getInstance()
            startTimePicker.show(
                    childFragmentManager, TimePickerFragment.TAG
            )
            startTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if(cal < calBeforeTimeDialog)
                {
                    Toast.makeText(context?.applicationContext,
                            getString(R.string.task_illegal_start_time_toast),
                            Toast.LENGTH_LONG).show()
                }
                else
                {
                    startCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                    startCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                    startTimeTextView.text = formatTime(cal)
                }
            })
        }
        endDateTextView.setOnClickListener{

            val bundle = Bundle()
            bundle.putInt("YEAR", endCal.get(Calendar.YEAR))
            bundle.putInt("MONTH", endCal.get(Calendar.MONTH))
            bundle.putInt("DAY", endCal.get(Calendar.DAY_OF_MONTH))
            endDatePicker.arguments = bundle

            endDatePicker.show(
                    childFragmentManager, DatePickerFragment.TAG
            )
            endDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if(cal < startCal)
                {
                    Toast.makeText(context?.applicationContext,
                            getString(R.string.task_illegal_end_date_toast),
                            Toast.LENGTH_LONG).show()
                }
                else
                {
                    endCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                    endDateTextView.text = formatDate(cal)
                }
            })
        }

        endTimeTextView.setOnClickListener{

            val bundle = Bundle()
            bundle.putInt("HOUR", endCal.get(Calendar.HOUR_OF_DAY))
            bundle.putInt("MINUTE", endCal.get(Calendar.MINUTE))
            endTimePicker.arguments = bundle

            endTimePicker.show(
                    childFragmentManager, TimePickerFragment.TAG
            )
            endTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                if(cal < startCal)
                {
                    Toast.makeText(context?.applicationContext,
                            getString(R.string.task_illegal_end_time_toast),
                            Toast.LENGTH_LONG).show()
                }
                else
                {
                    endCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                    endCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                    endTimeTextView.text = formatTime(cal)
                }

            })
        }

        val createButton = view.findViewById<Button>(R.id.taskCreate)
        createButton.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.nav_home)
        }
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
}