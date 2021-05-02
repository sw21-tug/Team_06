package com.team06.focuswork.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.model.LoggedInUser
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val TAG = "LOGIN_DATA_SOURCE"
    private val fireStoreUtil = FireBaseFireStoreUtil()
    fun login(username: String, password: String): Result<LoggedInUser> = try {
        val loggedInUser = fireStoreUtil.retrieveUser(username, password)
        Result.Success(loggedInUser)
    } catch (e: Throwable) {
        Log.e(TAG, "Couldn't log in user $username")
        e.printStackTrace()
        Result.Error(IOException("Error logging in", e))
    }

    fun logout() {
        // TODO: revoke authentication
    }

    fun register(username: String, password: String): Result<LoggedInUser> = try {
        fireStoreUtil.checkForExistingUser(username)
        val loggedInUser = fireStoreUtil.addUser(username, password)
        Result.Success(loggedInUser)
    } catch (e: Throwable) {
        Log.e(TAG, "Couldn't log in user $username")
        e.printStackTrace()
        Result.Error(IOException("Error logging in", e))
    }
}
