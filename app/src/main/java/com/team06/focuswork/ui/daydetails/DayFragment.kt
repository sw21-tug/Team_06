package com.team06.focuswork.ui.daydetails

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentDayBinding
import java.text.SimpleDateFormat
import java.util.*


private lateinit var binding: FragmentDayBinding
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DAY = "day"

/**
 * A simple [Fragment] subclass.
 * Use the [DayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var day: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            day = it.get(ARG_DAY) as Calendar
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cal = day ?: Calendar.getInstance()

        val day_name = SimpleDateFormat("EEEE").format(cal.time)
        val text = SpannableString(String.format("%s %s, %d.%d.", getString(R.string.day_tasks_for), day_name,
            cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1))
        text.setSpan(StyleSpan(Typeface.BOLD), getString(R.string.day_tasks_for).length+1,
            day_name.length + getString(R.string.day_tasks_for).length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textviewTitle.text = text

        FireBaseFireStoreUtil().retrieveTasks { tasks -> populateList(tasks) }
    }

    fun populateList(list: List<Task>)
    {
        /*
        //replace this with just list when the bloody stuff works
        val localList: MutableList<Task> = mutableListOf()
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, 1)
        val cal2 = Calendar.getInstance()
        cal2.add(Calendar.HOUR, 28)
        val task = Task("Task 1", "Description of Task 1",
                Calendar.getInstance(), cal)
        localList.add(task)
        localList.add(Task("Task 2", "Description of Task 2", cal, cal2))
        localList.add(task)
        localList.add(task)
        localList.add(task)
        localList.add(task)
        localList.add(task)
        localList.add(task)
        localList.add(task)
        localList.add(task)
        */

        val adapter = TaskAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        binding.progressbar.visibility = View.GONE
        binding.listTasks.adapter = adapter
        binding.listTasks.setOnItemClickListener {
                parent, view, position, id -> showTaskInfo(adapter.getItem(position))
        }
    }

    fun showTaskInfo(task: Task){
        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle(task.taskName)
        alertDialog.setMessage(String.format("%s\n%s - %s",
            task.taskDescription, getFormattedDate(task.startTime), getFormattedDate(task.endTime)))
        //alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    companion object {
        private fun getFormattedDate(cal: Calendar): String {
            return String.format("%d:%02d, %d.%d.%d", cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
        }
    }

    class TaskAdapter(context: Context, textViewResourceId: Int, val data: List<Task>)
        : ArrayAdapter<Task>(context, textViewResourceId){

        //API does not support streams/lambdas, saddening me
        private val tasks : List<Task>
        init{
            val localList: MutableList<Task> = mutableListOf()
            for (task in data)
            {
                //We don't care about the year-to-day conversion being accurate
                //since we only care about what day comes first, meaning leaving 1 day unsused
                //for most years is of no consequence
                val cal = Calendar.getInstance()
                val startDay = task.startTime.get(Calendar.DAY_OF_YEAR) +
                        task.startTime.get(Calendar.YEAR) * 366
                val endDay = task.endTime.get(Calendar.DAY_OF_YEAR) +
                        task.endTime.get(Calendar.YEAR) * 366
                val currentDay = cal.get(Calendar.DAY_OF_YEAR) +
                        cal.get(Calendar.YEAR) * 366

                if(currentDay in startDay..endDay)
                    localList.add(task)

            }
            tasks = localList
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): Task {
            return tasks[position]
        }

        fun getItems(): List<Task>{
            return tasks
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var currentTask = tasks[position]
            val label = super.getView(position, convertView, parent) as TextView
            label.setTextColor(Color.BLACK)
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f)
            val text = SpannableString(String.format("%s:\n%s %s\n%s %s", currentTask.taskName,
                context.getString(R.string.day_from), getFormattedDate(currentTask.startTime),
                context.getString(R.string.day_to), getFormattedDate(currentTask.endTime)))

            text.setSpan(StyleSpan(Typeface.BOLD), 0, currentTask.taskName.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            label.text = text
            return label
        }
    }
}