package com.team06.focuswork.data

import java.time.LocalDateTime

data class Task(var taskName: String, var taskDescription: String, var startTime: LocalDateTime, var duration: LocalDateTime)