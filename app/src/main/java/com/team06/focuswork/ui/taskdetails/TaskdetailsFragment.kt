package com.team06.focuswork.ui.taskdetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentTaskdetailsBinding
import com.team06.focuswork.model.TasksViewModel
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import java.util.HashMap

import java.util.*

class TaskdetailsFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskdetailsBinding
    private val fireBaseStore = FireBaseFireStoreUtil()
    private var taskTimer: CountDownTimer? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_detail_edit -> {
            findNavController().navigate(R.id.action_nav_taskdetails_to_nav_new_task)
            true
        }

        R.id.menu_detail_delete -> {
            onDeleteItem()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentTaskdetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleView: TextView = binding.titleTaskdetails
        val descriptionView: TextView = binding.descriptionTaskdetails
        val starttimeView: TextView = binding.starttimeTaskdetails
        val endtimeView: TextView = binding.endtimeTaskdetails
        val startdateView: TextView = binding.startdateTaskdetails
        val enddateView: TextView = binding.enddateTaskdetails

        val timeFormat: java.text.DateFormat = DateFormat.getTimeFormat(context)
        val dateFormat: java.text.DateFormat = DateFormat.getDateFormat(context)

        binding.taskTimer.text = "00:00"
        taskTimer?.cancel()

        tasksViewModel.currentTask.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                titleView.text = it.taskName
                descriptionView.text = it.taskDescription
                starttimeView.text = timeFormat.format(it.startTime.time)
                endtimeView.text = timeFormat.format(it.endTime.time)
                startdateView.text = dateFormat.format(it.startTime.time)
                enddateView.text = dateFormat.format(it.endTime.time)

                onStartTimer(it)
            }
        })

    }

    private fun onStartTimer(task: Task){
        val duration = task.endTime.timeInMillis - Calendar.getInstance().timeInMillis

        taskTimer = object : CountDownTimer(duration, 10 * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (60 * 60 * 1000)
                val minutes = millisUntilFinished / (60 * 1000) % (60)
                binding.taskTimer.text = String.format("%02d:%02d", hours, minutes)
                updateTimerAnimation(Calendar.getInstance().timeInMillis,
                    task.startTime.timeInMillis, task.endTime.timeInMillis);
            }

            override fun onFinish() {
                return // currently no action

            /*
                Toast.makeText(context?.applicationContext, "Task finished",
                    Toast.LENGTH_LONG).show()
            */
            }
        }.start()
    }

    private fun updateTimerAnimation(currentTimeInMillis: Long, startTimeInMillis: Long,
                                     endTimeInMillis: Long) {
        if (currentTimeInMillis <= startTimeInMillis) {
            binding.timerAnimation.progress = 100
        } else if (currentTimeInMillis >= endTimeInMillis) {
            binding.timerAnimation.progress = 0
        } else {
            val difference = (endTimeInMillis - currentTimeInMillis).toFloat()
            val totalTime = (endTimeInMillis - startTimeInMillis).toFloat()

            binding.timerAnimation.progress = ((difference / totalTime) * 100).toInt()
        }
    }

    private fun onDeleteItem() {
        val deleteDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    R.string.delete_dialog_confirm_delete,
                    DialogInterface.OnClickListener { dialog, _ ->
                        onConfirmDelete()
                        dialog.dismiss()
                    })

                setNegativeButton(
                    R.string.delete_dialog_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
            }

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.delete_dialog_description)
                .setTitle(R.string.delete_dialog_title)

            // Create the AlertDialog
            builder.create()
        }

        deleteDialog?.show()
    }

    private fun onConfirmDelete() {
        fireBaseStore.deleteTask(tasksViewModel.currentTask.value!!)
        findNavController().navigateUp()
    }
}