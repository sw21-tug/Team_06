package com.team06.focuswork.ui.overview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.team06.focuswork.R

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val list = ('A').rangeTo('Z').toList()

    init {
        Log.d("TaskAdapter", list.toString())
    }

    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val taskItem = view.findViewById<TextView>(R.id.task_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_item_view, parent, false)
        return TaskViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]
        holder.taskItem.text = item.toString()
        Log.d("TaskAdapter", item.toString())
        holder.taskItem.setOnClickListener {
            Toast.makeText(holder.view.context, item.toString(), Toast.LENGTH_LONG).show()
            //TODO: open new task fragment here
        }
    }

    override fun getItemCount() = list.size

}
