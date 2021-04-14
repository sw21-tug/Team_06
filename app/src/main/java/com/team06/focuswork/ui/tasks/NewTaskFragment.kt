package com.team06.focuswork.ui.tasks

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.ui.util.DatePickerFragment
import com.team06.focuswork.ui.util.TimePickerFragment
import java.util.*

class NewTaskFragment : Fragment() {

    private lateinit var workingTask: Task
    private val dpf = DatePickerFragment()
    private val tpf = TimePickerFragment()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startDatePicker = view.findViewById<TextView>(R.id.taskStartDate)
        val startTimePicker = view.findViewById<TextView>(R.id.taskStartTime)

        startDatePicker.setOnClickListener{
            dpf.show(
                    childFragmentManager, DatePickerFragment.TAG
            )
            dpf.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                startDatePicker.text = "".plus(cal.get(Calendar.DAY_OF_MONTH)).plus(". ")
                        .plus(cal.get(Calendar.MONTH)+1).plus(". ")
                        .plus(cal.get(Calendar.YEAR))
            })
        }
        startTimePicker.setOnClickListener{
            tpf.show(
                    childFragmentManager, TimePickerFragment.TAG
            )
            tpf.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val cal = it ?: return@Observer
                startTimePicker.text = "".plus(cal.get(Calendar.HOUR)).plus(":")
                        .plus(cal.get(Calendar.MINUTE))
            })
        }

        //val usernameEditText = view.findViewById<EditText>(R.id.taskName)
        //val passwordEditText = view.findViewById<EditText>(R.id.taskDescription)
        //val loginButton = view.findViewById<Button>(R.id.login)
        //val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)



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