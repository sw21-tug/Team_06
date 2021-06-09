package com.team06.focuswork.ui.overview

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team06.focuswork.R


class TaskAdapter(private val context: Context, private val overviewFragment: OverviewFragment) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val list = overviewFragment.getAllTasks()

    init {
        Log.d("TaskAdapter", list.toString())
    }

    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val taskItem: MaterialCardView = view.findViewById(R.id.task_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layout = LayoutInflater
            .from(parent.context).inflate(R.layout.task_item_view, parent, false)
        return TaskViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]
        val dateFormat: java.text.DateFormat? = DateFormat.getTimeFormat(holder.view.context)

        holder.taskItem.findViewById<TextView>(R.id.task_item_title).text = item.taskName
        val startTimeText: TextView = holder.taskItem.findViewById(R.id.task_item_start_time)
        startTimeText.text = dateFormat?.format(item.startTime.time)
        val endTimeTextView: TextView = holder.taskItem.findViewById(R.id.task_item_end_time)
        endTimeTextView.text = dateFormat?.format(item.endTime.time)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.cardBackgroundColor, typedValue, true)
        holder.taskItem.setCardBackgroundColor(typedValue.data)

        Log.d("TaskAdapter", item.taskName)
        holder.taskItem.setOnClickListener {
            overviewFragment.onClickTaskItem(item)
        }

        holder.taskItem.tag = "Task:$position"
    }

    override fun getItemCount() = list.size

}
