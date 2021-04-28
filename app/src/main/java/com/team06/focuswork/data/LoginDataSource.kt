package com.team06.focuswork.data

import android.util.Log
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.team06.focuswork.model.LoggedInUser
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    val TAG = "LOGIN_DATA_SOURCE"
    var firebaseStore = FirebaseFirestore.getInstance()
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val asyncTask = firebaseStore
                .collection("User")
                .whereEqualTo("email", username)
                .whereEqualTo("password", password)
                .get()
            while(!asyncTask.isComplete);
            val documents = asyncTask.result
            val loggedInUser = LoggedInUser(username)
            documents!!.forEach {
                val workingTask = Task(
                    it.getString("name")!!,
                    it.getString("description")!!,
                    CalendarTimestampUtil.toCalendar(it.getTimestamp("startTime")!!),
                    CalendarTimestampUtil.toCalendar(it.getTimestamp("endTime")!!)
                )
                loggedInUser.tasks.add(workingTask)
            }
            return Result.Success(loggedInUser)
        } catch (e: Throwable) {
            Log.e(TAG, "Couldn't log in user $username")
            e.printStackTrace()
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}