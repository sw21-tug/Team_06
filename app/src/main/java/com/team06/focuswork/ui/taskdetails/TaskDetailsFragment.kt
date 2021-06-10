package com.team06.focuswork.ui.taskdetails

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.Task
import com.team06.focuswork.databinding.FragmentTaskdetailsBinding
import com.team06.focuswork.model.TasksViewModel
import java.util.*

class TaskDetailsFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskdetailsBinding
    private val fireBaseStore = FireBaseFireStoreUtil()
    private var taskTimer: CountDownTimer? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_detail_edit -> {
            findNavController().navigate(R.id.action_nav_taskdetails_to_nav_new_task)
            true
        }
        R.id.menu_detail_delete -> {
            onDeleteItem()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentTaskdetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeFormat: java.text.DateFormat = DateFormat.getTimeFormat(context)
        val dateFormat: java.text.DateFormat = DateFormat.getDateFormat(context)

        binding.taskTimer.text = requireContext().getString(R.string.timer_default_value)
        taskTimer?.cancel()

        tasksViewModel.currentTask.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.titleTaskdetails.text = it.taskName
                binding.descriptionTaskdetails.text = it.taskDescription
                binding.starttimeTaskdetails.text = timeFormat.format(it.startTime.time)
                binding.endtimeTaskdetails.text = timeFormat.format(it.endTime.time)
                binding.startdateTaskdetails.text = dateFormat.format(it.startTime.time)
                binding.enddateTaskdetails.text = dateFormat.format(it.endTime.time)
                onStartTimer(it)
            }
        })
    }

    private fun onStartTimer(task: Task) {
        val duration = task.endTime.timeInMillis - Calendar.getInstance().timeInMillis

        taskTimer = object : CountDownTimer(duration, 500) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (60 * 60 * 1000)
                val minutes = millisUntilFinished / (60 * 1000) % (60)
                binding.taskTimer.text = String.format("%02d:%02d", hours, minutes)
                updateTimerAnimation(
                    Calendar.getInstance().timeInMillis,
                    task.startTime.timeInMillis, task.endTime.timeInMillis
                )
            }

            override fun onFinish() {
            }
        }.start()
    }

    internal fun updateTimerAnimation(
        currentTimeInMillis: Long, startTimeInMillis: Long, endTimeInMillis: Long
    ) {
        when {
            currentTimeInMillis <= startTimeInMillis -> binding.timerAnimation.progress = 100
            currentTimeInMillis >= endTimeInMillis -> binding.timerAnimation.progress = 0
            else -> {
                val difference = (endTimeInMillis - currentTimeInMillis).toFloat()
                val totalTime = (endTimeInMillis - startTimeInMillis).toFloat()
                binding.timerAnimation.progress = ((difference / totalTime) * 100).toInt()
            }
        }
    }

    private fun onDeleteItem() {
        val deleteDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.delete_dialog_confirm_delete) { dialog, _ ->
                    onConfirmDelete()
                    dialog.dismiss()
                }
                setNegativeButton(R.string.delete_dialog_cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }

            builder.setMessage(R.string.delete_dialog_description)
                .setTitle(R.string.delete_dialog_title)
            builder.create()
        }

        deleteDialog?.show()
    }

    private fun onConfirmDelete() {
        tasksViewModel.currentTask.value?.let { fireBaseStore.deleteTask(it) }
        findNavController().navigateUp()
    }
}