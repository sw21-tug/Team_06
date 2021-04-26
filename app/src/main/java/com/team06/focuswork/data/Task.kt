package com.team06.focuswork.data

import java.io.Serializable
import java.util.*

data class Task(var taskName: String, var taskDescription: String, var startTime: Calendar, var endTime: Calendar)