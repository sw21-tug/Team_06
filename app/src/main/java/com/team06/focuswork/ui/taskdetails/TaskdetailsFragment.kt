package com.team06.focuswork.ui.taskdetails

import android.os.Bundle
import android.text.Layout
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.team06.focuswork.R
import com.team06.focuswork.databinding.FragmentTaskdetailsBinding
import com.team06.focuswork.model.TasksViewModel

class TaskdetailsFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskdetailsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
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
}