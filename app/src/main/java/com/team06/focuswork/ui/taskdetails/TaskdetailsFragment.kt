package com.team06.focuswork.ui.taskdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.team06.focuswork.R
import com.team06.focuswork.model.TasksViewModel

class TaskdetailsFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_taskdetails, container, false)

        val titleView: TextView = root.findViewById(R.id.title_of_taskdetails)
        tasksViewModel.title.observe(viewLifecycleOwner, Observer {
            titleView.text = it
        })

        val descriptionView: TextView = root.findViewById(R.id.description_of_taskdetails)
        tasksViewModel.description.observe(viewLifecycleOwner, Observer {
            descriptionView.text = it
        })

        val starttimeView: TextView = root.findViewById(R.id.starttime_of_taskdetails)
        tasksViewModel.startTime.observe(viewLifecycleOwner, Observer {
            starttimeView.text = it
        })

        val durationView: TextView = root.findViewById(R.id.duration_of_taskdetails)
        tasksViewModel.endTime.observe(viewLifecycleOwner, Observer {
            durationView.text = it
        })

        return root
    }
}