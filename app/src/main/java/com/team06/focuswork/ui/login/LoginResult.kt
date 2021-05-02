package com.team06.focuswork.ui.login

import com.team06.focuswork.model.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: LoggedInUser? = null,
        val error: Int? = null
)