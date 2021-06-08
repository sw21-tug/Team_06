package com.team06.focuswork.data

import com.team06.focuswork.model.LoggedInUser
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object LoginRepository {
    var dataSource: LoginDataSource? = null
    var user: LoggedInUser? = null

    fun logout() {
        user = null
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource?.login(username, password)

        if (result is Result.Success) {
            this.user = result.data
        }

        return result ?: Result.Error(IOException("Error logging in"))
    }

    fun register(
        firstname: String, lastname: String, username: String, password: String
    ): Result<LoggedInUser> {
        val result = dataSource?.register(firstname, lastname, username, password)

        if (result is Result.Success) {
            this.user = result.data
        }

        return result ?: Result.Error(IOException("Error logging in"))
    }
}