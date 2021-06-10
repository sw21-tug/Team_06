package com.team06.focuswork.data

import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.model.LoggedInUser
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import java.util.*

class FireBaseFireStoreUtil {
    enum class Filter {
        NONE, DAY, WEEK, MONTH, ALL
    }

    private val fireBaseStore = FirebaseFirestore.getInstance()
    private val userCollection = "User"
    private val taskCollection = "Task"
    private val emailField = "email"
    private val passwordField = "password"
    private val nameField = "firstname"
    private val surNameField = "lastname"


    fun retrieveUser(username: String, password: String): LoggedInUser {
        val fetchUserAsync = fireBaseStore.collection(userCollection)
            .whereEqualTo(emailField, username).whereEqualTo(passwordField, password).get()

        while (!fetchUserAsync.isComplete);
        val documents = fetchUserAsync.result?.documents?.get(0)?.id ?: throw Throwable()
        return LoggedInUser(documents)
    }

    fun retrieveTasks(callback: (tasks: List<Task>) -> Unit) {
        val taskCollection = fireBaseStore.collection(userCollection)
            .document((LoginRepository.user ?: return).userId)
            .collection(taskCollection).orderBy("startTime")

        taskCollection.get().addOnSuccessListener { tasks ->
            val taskList: MutableList<Task> = mutableListOf()
            (tasks ?: return@addOnSuccessListener).forEach {
                val workingTask = Task(
                    it.getString("name") ?: return@forEach,
                    it.getString("description") ?: return@forEach,
                    CalendarTimestampUtil.toCalendar(
                        it.getTimestamp("startTime") ?: return@forEach
                    ),
                    CalendarTimestampUtil.toCalendar(it.getTimestamp("endTime") ?: return@forEach)
                )
                workingTask.id = it.id
                taskList.add(workingTask)
            }
            callback(taskList)
        }
    }

    fun checkForExistingUser(username: String) {
        val fetchUserAsync = fireBaseStore.collection(userCollection)
            .whereEqualTo(emailField, username).get()

        while (!fetchUserAsync.isComplete);
        val documents = fetchUserAsync.result?.documents ?: throw Throwable()
        if (documents.size > 0) {
            throw Throwable()
        }
    }

    fun addUser(
        firstname: String, lastname: String, username: String, password: String
    ): LoggedInUser {
        val user: MutableMap<String, Any> = HashMap()
        user[nameField] = firstname
        user[surNameField] = lastname
        user[emailField] = username
        user[passwordField] = password
        val documents = fireBaseStore.collection(userCollection).add(user)
        while (!documents.isComplete);
        val result = documents.result ?: throw Throwable()
        return LoggedInUser(result.id)
    }

    fun saveTask(task: Task, callback: (currentTask: Task) -> Unit) {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = task.taskName
        map["description"] = task.taskDescription
        map["startTime"] = CalendarTimestampUtil.toTimeStamp(task.startTime)
        map["endTime"] = CalendarTimestampUtil.toTimeStamp(task.endTime)
        val db = FirebaseFirestore.getInstance()
        LoginRepository.user?.userId?.let {
            val collection = db.collection("User").document(it).collection("Task")
            if (task.id.isEmpty()) {
                collection.add(map).addOnSuccessListener { documentReference ->
                    task.id = documentReference.id
                    callback(task)
                }
            } else {
                collection.document(task.id).set(map)
                callback(task)
            }
        }
    }

    fun deleteTask(currentTask: Task) {
        val userId = LoginRepository.user?.userId ?: return
        fireBaseStore.collection("User")
            .document(userId).collection("Task").document(currentTask.id).delete()
    }
}