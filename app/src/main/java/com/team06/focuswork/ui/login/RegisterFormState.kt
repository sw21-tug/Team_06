package com.team06.focuswork.ui.login

/**
 * Data validation state of the register form.
 */
data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val firstnameError: Int? = null,
    val lastnameError: Int? = null,
    val isDataValid: Boolean = false
)