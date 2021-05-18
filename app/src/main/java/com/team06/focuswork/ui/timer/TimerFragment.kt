package com.team06.focuswork.ui.timer

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentTimerBinding
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val list = mutableListOf<Task>()
    init {
        populateList()
    }

    private var selectedTask : Task? = null
    private var selectedTaskTimer : CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateSpinner()

        binding.buttonStartTimer.setOnClickListener{
            if(selectedTask == null)
            {
                showToast(getString(R.string.no_task_selected))
                return@setOnClickListener
            }
            val duration = selectedTask!!.endTime.timeInMillis -
                    selectedTask!!.startTime.timeInMillis

            selectedTaskTimer = object: CountDownTimer(duration, 10 * 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / (60 * 60 * 1000)
                    val minutes = millisUntilFinished / (60 * 1000) % (60)
                    binding.taskTimer.text = String.format("%d:%02d", hours, minutes)
                }

                override fun onFinish() {
                    showToast(getString(R.string.timer_finished))
                }
            }.start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun populateList(){
        //Intended: call data holder class to fetch Tasks
        val calLater = Calendar.getInstance()
        calLater.add(Calendar.HOUR, 1)
        list.add(
            Task(
                "Erste Aufgabe",
                "Dies ist eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Zweite Aufgabe",
                "Dies ist weitere Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Dritte Aufgabe",
                "Dies ist noch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Vierte Aufgabe",
                "Dies ist auch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Fuenfte Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
        list.add(
            Task(
                "Sechste Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                calLater
            )
        )
    }

    fun populateSpinner(){
        val spinner = binding.taskSelector
        val adapter = TaskArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        )
        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                selectedTask = adapter.getItem(position)
                selectedTaskTimer?.cancel()
                binding.taskTimer.text = "0:00"
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                selectedTask = null
                selectedTaskTimer?.cancel()
                binding.taskTimer.text = "0:00"
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context?.applicationContext, message, Toast.LENGTH_LONG).show()
    }
}

class TaskArrayAdapter(context: Context, textViewResourceId: Int, val tasks: MutableList<Task>)
    : ArrayAdapter<Task>(context, textViewResourceId) {

    override fun getCount() : Int {
        return tasks.size
    }

    override fun getItem(position: Int) : Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0f)
        label.setText(tasks[position].taskName)

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Could be changed if dropdown should have a different style from the 'passive'
        return getView(position, convertView, parent)
    }
}