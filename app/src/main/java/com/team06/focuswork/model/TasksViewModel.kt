package com.team06.focuswork.model

import android.content.Context
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team06.focuswork.data.Task

class TasksViewModel : ViewModel() {

    private var _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private var _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> = _startTime

    private var _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> = _endTime

    private var _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> = _allTasks

    fun setSelectedTask(task: Task, context: Context) {
        val dateFormat: java.text.DateFormat = DateFormat.getTimeFormat(context)
        _title.apply { value = task.taskName}
        _description.apply { value = task.taskDescription }
        _startTime.apply { value = dateFormat.format(task.startTime.time) }
        _endTime.apply { value = dateFormat.format(task.endTime.time) }
    }

    fun setTasks(tasks: List<Task>) {
        _allTasks.apply { value = tasks }
    }
}