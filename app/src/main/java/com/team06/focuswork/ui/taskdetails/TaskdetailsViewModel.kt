package com.team06.focuswork.ui.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime


data class Task(var taskName: String, var taskDescription: String )//var startTime: LocalDateTime, var duration: LocalDateTime)

var task1 = Task("erste Aufgabe",
                "Das ist unsere erste Aufgabe. Sie muss erledigt werden.")

class TaskdetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Taskdetails Fragment"
    }
    val text: LiveData<String> = _text

    private val _title = MutableLiveData<String>().apply {
        value = task1.taskName
    }
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>().apply {
        value = task1.taskDescription
    }
    val description: LiveData<String> = _description

    /*private val _starttime = MutableLiveData<String>().apply {
        LocalDateTime value = LocalDateTime.now()
    }
    val starttime: LiveData<String> = _starttime

    private val _duration = MutableLiveData<String>().apply {
        LocalDateTime value = '2021-04-30 T
    }
    val duration: LiveData<String> = _duration*/
}