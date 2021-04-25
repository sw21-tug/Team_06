package com.team06.focuswork.ui.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



data class Task(var taskName: String, var taskDescription: String )//var startTime: LocalDateTime, var duration: LocalDateTime)

var current_task = Task("erste Aufgabe",
                "Das ist unsere erste Aufgabe. Sie muss erledigt werden.")

class TaskdetailsViewModel : ViewModel() {

    private val _title = MutableLiveData<String>().apply {
        value = current_task.taskName
    }
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>().apply {
        value = current_task.taskDescription
    }
    val description: LiveData<String> = _description

    private val _starttime = MutableLiveData<String>().apply {
        value = "15 Uhr"
    }
    val starttime: LiveData<String> = _starttime

    private val _duration = MutableLiveData<String>().apply {
        value = "17 Uhr"
    }
    val duration: LiveData<String> = _duration
}