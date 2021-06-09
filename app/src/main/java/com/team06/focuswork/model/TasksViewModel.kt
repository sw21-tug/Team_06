package com.team06.focuswork.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team06.focuswork.data.Task
import com.team06.focuswork.ui.util.NotificationUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    private var _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> = _allTasks

    private var _currentTask = MutableLiveData<Task?>()
    val currentTask: LiveData<Task?> = _currentTask

    fun setSelectedTask(task: Task?) {
        _currentTask.postValue(task)
    }

    fun setTasks(tasks: List<Task>) {
        _allTasks.postValue(tasks)
    }
}