package com.team06.focuswork.ui.login

import com.team06.focuswork.model.TasksViewModel

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: TasksViewModel? = null,
    val error: Int? = null
)