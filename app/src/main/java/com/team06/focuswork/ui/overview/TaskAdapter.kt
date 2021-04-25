package com.team06.focuswork.ui.overview

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import com.team06.focuswork.ui.taskdetails.TaskdetailsFragment
import java.util.*


class TaskAdapter(private val context: Context, private val overviewFragment: Fragment)
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val list = mutableListOf<Task>()

    init {
        list.add(
            Task(
                "Erste Aufgabe",
                "Dies ist eine Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )
        list.add(
            Task(
                "Zweite Aufgabe",
                "Dies ist weitere Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )
        list.add(
            Task(
                "Dritte Aufgabe",
                "Dies ist noch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )
        list.add(
            Task(
                "Vierte Aufgabe",
                "Dies ist auch eine Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )
        list.add(
            Task(
                "Fuenfte Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )
        list.add(
            Task(
                "Sechste Aufgabe",
                "Dies ist eine tolle Aufgabenbeschr.",
                Calendar.getInstance(),
                Calendar.getInstance()
            )
        )

        Log.d("TaskAdapter", list.toString())
    }

    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val taskItem = view.findViewById<MaterialCardView>(R.id.task_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_item_view, parent, false)
        return TaskViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]
        val dateFormat: java.text.DateFormat? = DateFormat.getTimeFormat(holder.view.context)

        holder.taskItem.findViewById<TextView>(R.id.task_item_title).text = item.taskName
        val startTimeText : TextView = holder.taskItem.findViewById(R.id.task_item_start_time)
        startTimeText.text = dateFormat?.format(item.startTime.time)
        val endTimeTextView : TextView = holder.taskItem.findViewById(R.id.task_item_end_time)
        endTimeTextView.text = dateFormat?.format(item.duration.time)

        holder.taskItem.background = if (position % 2 == 0)
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_rounded_corners_heavy,
                null
            )
        else
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_rounded_corners_light,
                null
            )

        Log.d("TaskAdapter", item.taskName)
        holder.taskItem.setOnClickListener {
            Toast.makeText(
                holder.view.context,
                item.taskName + ": " + item.taskDescription,
                Toast.LENGTH_LONG
            ).show()

            val ft: FragmentTransaction =
                overviewFragment.childFragmentManager.beginTransaction()
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            val fragment2 = TaskdetailsFragment()
            val bundle = Bundle()
                bundle.putSerializable("task", item)
                fragment2.arguments = bundle
                ft.replace(R.id.fragment_container_taskdetails, fragment2)
                ft.addToBackStack(null)
                ft.commit()

            /*holder.view.findNavController().navigate(
                R.id.action_nav_overview_to_nav_taskdetails,
                item
            )*/


            //TODO: open new task fragment here
        }
    }

    override fun getItemCount() = list.size

}
