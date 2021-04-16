package com.team06.focuswork.ui.taskdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.team06.focuswork.R

class TaskdetailsFragment : Fragment() {

    private lateinit var taskdetailsViewModel: TaskdetailsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        taskdetailsViewModel =
                ViewModelProvider(this).get(TaskdetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_taskdetails, container, false)
        val textView: TextView = root.findViewById(R.id.text_taskdetails)
        taskdetailsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val titleView: TextView = root.findViewById(R.id.title_of_taskdetails)
        taskdetailsViewModel.title.observe(viewLifecycleOwner, Observer {
            titleView.text = it
        })

        val descriptionView: TextView = root.findViewById(R.id.description_of_taskdetails)
        taskdetailsViewModel.description.observe(viewLifecycleOwner, Observer {
            descriptionView.text = it
        })
        return root
    }
}