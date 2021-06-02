package com.team06.focuswork.ui.overview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
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

        holder.taskItem.background = if (position % 2 == 0)
            chooseBackGround(R.drawable.rectangle_rounded_corners_heavy) else
            chooseBackGround(R.drawable.rectangle_rounded_corners_light)

        Log.d("TaskAdapter", item.taskName)
        holder.taskItem.setOnClickListener {
            showToast(item.taskName + ": " + item.taskDescription)
            overviewFragment.onClickTaskItem(item)
        }

        holder.taskItem.tag = "Task:$position"
    }

    private fun chooseBackGround(drawableId: Int): Drawable? = ResourcesCompat.getDrawable(
        context.resources, drawableId, null
    )

    private fun showToast(message: String) {
        Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun getItemCount() = list.size

}
