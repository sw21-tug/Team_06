package com.team06.focuswork.ui.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskdetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Taskdetails Fragment"
    }
    val text: LiveData<String> = _text
}