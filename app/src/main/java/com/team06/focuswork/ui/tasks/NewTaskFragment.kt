package com.team06.focuswork.ui.tasks

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startDateTextView = view.findViewById<TextView>(R.id.taskStartDate)
        val startTimeTextView = view.findViewById<TextView>(R.id.taskStartTime)
        val endDateTextView = view.findViewById<TextView>(R.id.taskEndDate)
        val endTimeTextView = view.findViewById<TextView>(R.id.taskEndTime)
        val initialTime = Calendar.getInstance()
        startDateTextView.text = formatDate(initialTime)
        startTimeTextView.text = formatTime(initialTime)
        initialTime.add(Calendar.HOUR, 1)
        endDateTextView.text = formatDate(initialTime)
        endTimeTextView.text = formatTime(initialTime)

        startDateTextView.setOnClickListener{
            startDatePicker.show(
                    childFragmentManager, DatePickerFragment.TAG
            )
            startDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                startDateTextView.text = formatDate(cal)
            })
        }
        startTimeTextView.setOnClickListener{
            startTimePicker.show(
                    childFragmentManager, TimePickerFragment.TAG
            )
            startTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                startTimeTextView.text = formatTime(cal)
            })
        }
        endDateTextView.setOnClickListener{
            endDatePicker.show(
                    childFragmentManager, DatePickerFragment.TAG
            )
            endDatePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                endDateTextView.text = formatDate(cal)
            })
        }
        endTimeTextView.setOnClickListener{
            endTimePicker.show(
                    childFragmentManager, TimePickerFragment.TAG
            )
            endTimePicker.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                endTimeTextView.text = formatTime(cal)
            })
        }

        //val usernameEditText = view.findViewById<EditText>(R.id.taskName)
        //val passwordEditText = view.findViewById<EditText>(R.id.taskDescription)
        //val loginButton = view.findViewById<Button>(R.id.login)
        //val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
    }
    private fun formatDate(cal: Calendar): String {
        return getDateInstance(MEDIUM).format(cal.time)
        /*return "".plus(cal.get(Calendar.DAY_OF_MONTH)).plus(". ")
                 .plus(cal.get(Calendar.MONTH)+1).plus(". ")
                 .plus(cal.get(Calendar.YEAR))*/
    }
    private fun formatTime(cal: Calendar): String {
        return getTimeInstance(SHORT).format(cal.time)
        /*return "".plus(cal.get(Calendar.HOUR_OF_DAY)).plus(":")
                 .plus(cal.get(Calendar.MINUTE))*/
    }
    private fun updateUiWithUser(model: Task) {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}