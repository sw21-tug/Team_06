package com.team06.focuswork.ui.taskdetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.databinding.FragmentTaskdetailsBinding
import com.team06.focuswork.model.TasksViewModel
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import java.util.HashMap


class TaskdetailsFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskdetailsBinding
    private val fireBaseStore = FireBaseFireStoreUtil()

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

        tasksViewModel.currentTask.observe(viewLifecycleOwner, Observer {
            titleView.text = it.taskName
            descriptionView.text = it.taskDescription
            starttimeView.text = timeFormat.format(it.startTime.time)
            endtimeView.text = timeFormat.format(it.endTime.time)
            startdateView.text = dateFormat.format(it.startTime.time)
            enddateView.text = dateFormat.format(it.endTime.time)

        })

    }
    private fun onDeleteItem() {
        val deleteDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.delete_dialog_confirm_delete,
                        DialogInterface.OnClickListener { dialog, _ ->
                            onConfirmDelete()
                            dialog.dismiss()
                        })

                setNegativeButton(R.string.delete_dialog_cancel,
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