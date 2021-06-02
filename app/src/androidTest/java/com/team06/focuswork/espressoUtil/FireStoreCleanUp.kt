package com.team06.focuswork.espressoUtil

import com.google.firebase.firestore.FirebaseFirestore
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

    fun deleteUser(username: String) {
        FirebaseFirestore.getInstance()
            .collection("User")
            .whereEqualTo("email", username)
            .get().addOnSuccessListener { document ->
                assert(document.documents.size < 2)
                if (document.documents.isEmpty()) {
                    return@addOnSuccessListener
                }
                FirebaseFirestore
                    .getInstance()
                    .collection("User")
                    .document(document.documents[0].id)
                    .delete()
            }
    }
}

//Test Users: NewTaskInstrumentedTest: id = dggkbNlMM7QqSWjj8Nii
//            LogIninstrumentedTest: username = "test@gmail.com", password = "password"
//            OverViewDetailsInstrumentedTest: id = SfuvPQ8Uf2wistKapXBQ