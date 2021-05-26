package com.team06.focuswork.espressoUtil

import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.Task
import org.awaitility.Awaitility
import java.util.concurrent.TimeUnit

object FireStoreCleanUp {
    private var asyncTaskFinished = false
    private val util = FireBaseFireStoreUtil()

    fun deleteAllTasksOfCurrentUser() {
        util.retrieveTasks(this::deleteTasks)
        Awaitility.await().atMost(5, TimeUnit.SECONDS).until { asyncTaskFinished }
        asyncTaskFinished = false
    }

    private fun deleteTasks(tasks: List<Task>) {
        tasks.forEach { util.deleteTask(it) }
        asyncTaskFinished = true
    }
}