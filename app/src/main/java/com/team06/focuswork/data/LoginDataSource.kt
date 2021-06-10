@file:Suppress("PrivatePropertyName")

package com.team06.focuswork.data

import android.util.Log
import com.team06.focuswork.model.LoggedInUser
import java.io.IOException

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
        Log.e(
            TAG,
            "Unfortunately the user could not be logged in! Username: $username, Password: $password"
        )
        e.printStackTrace()
        Result.Error(IOException("Error logging in", e))
    }

    fun register(
        firstname: String, lastname: String, username: String, password: String
    ): Result<LoggedInUser> = try {
        fireStoreUtil.checkForExistingUser(username)
        val loggedInUser = fireStoreUtil.addUser(firstname, lastname, username, password)
        Result.Success(loggedInUser)
    } catch (e: Throwable) {
        Log.e(
            TAG,
            "Unfortunately the user could not be registered! Username: $username, Password: $password"
        )
        e.printStackTrace()
        Result.Error(IOException("Error logging in", e))
    }
}
