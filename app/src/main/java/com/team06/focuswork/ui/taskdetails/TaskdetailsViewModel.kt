package com.team06.focuswork.ui.taskdetails

import android.content.Context
import android.text.format.DateFormat
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team06.focuswork.R
import com.team06.focuswork.data.Task
import java.util.*

class TaskdetailsViewModel() : ViewModel() {

    private var _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private var _starttime = MutableLiveData<String>()
    val starttime: LiveData<String> = _starttime

    private var _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> = _endTime

    fun setAll(task: Task, context: Context?) {
         val dateFormat: java.text.DateFormat = DateFormat.getTimeFormat(context)
        _title.apply { value = task.taskName}
        _description.apply { value = task.taskDescription }
        _starttime.apply { value = dateFormat.format(task.startTime.time) }
        _endTime.apply { value = dateFormat.format(task.endTime.time) }
    }
}