package com.team06.focuswork.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.model.LoggedInUser
import com.team06.focuswork.ui.util.CalendarTimestampUtil
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val TAG = "LOGIN_DATA_SOURCE"
    var firebaseStore = FirebaseFirestore.getInstance()
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val asyncTask = firebaseStore
                .collection("User")
                .whereEqualTo("email", username)
                .whereEqualTo("password", password)
                .get()
            while(!asyncTask.isComplete);
            val documents = asyncTask.result?.documents?.get(0)?.id
            val loggedInUser = LoggedInUser(documents!!)

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

    fun register(username: String, password: String): Result<LoggedInUser> {
        try {
            val asyncTask = firebaseStore
                    .collection("User")
                    .whereEqualTo("email", username)
                    .get()
            while(!asyncTask.isComplete);

            if (asyncTask.result!!.documents.size > 0) {
                return Result.Error(Exception("Error logging in"))
            }

            val user: MutableMap<String, Any> = HashMap()
            user["email"] = username
            user["password"] = password
            val documents  = firebaseStore.collection("User").add(user)
            while(!documents.isComplete);
            val loggedInUser = LoggedInUser(documents.result!!.id)
            return Result.Success(loggedInUser)
        } catch (e: Throwable) {
            Log.e(TAG, "Couldn't log in user $username")
            e.printStackTrace()
            return Result.Error(IOException("Error logging in", e))
        }
    }
}