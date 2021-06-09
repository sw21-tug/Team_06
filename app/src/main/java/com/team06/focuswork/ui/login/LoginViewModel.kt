package com.team06.focuswork.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.data.Result
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    enum class FormState { ERR_FIRSTNAME, ERR_LASTNAME, ERR_USERNAME, ERR_PASSWORD, VALID }
    enum class LoginState { ERROR, SUCCESS }

    private val _loginForm = MutableLiveData<FormState>()
    val loginFormState: LiveData<FormState> = _loginForm

    private val _registerForm = MutableLiveData<FormState>()
    val registerFormState: LiveData<FormState> = _registerForm

    private val _loginResult = MutableLiveData<LoginState>()
    val loginResult: LiveData<LoginState> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch { // can be launched in a separate asynchronous job
            val result = LoginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value = LoginState.SUCCESS
            } else {
                _loginResult.value = LoginState.ERROR
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = FormState.ERR_USERNAME
        } else if (!isPasswordValid(password)) {
            _loginForm.value = FormState.ERR_PASSWORD
        } else {
            _loginForm.value = FormState.VALID
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun register(firstname: String, lastname: String, username: String, password: String) {
        viewModelScope.launch { // can be launched in a separate asynchronous job
            val result = LoginRepository.register(firstname, lastname, username, password)

            if (result is Result.Success) {
                _loginResult.value = LoginState.SUCCESS
            } else {
                _loginResult.value = LoginState.ERROR
            }
        }
    }

    fun registerDataChanged(
        firstname: String, lastname: String, username: String, password: String
    ) {
        if (firstname.isBlank()) {
            _registerForm.value = FormState.ERR_FIRSTNAME
        } else if (lastname.isBlank()) {
            _registerForm.value = FormState.ERR_LASTNAME
        } else if (!isUserNameValid(username)) {
            _registerForm.value = FormState.ERR_USERNAME
        } else if (!isPasswordValid(password)) {
            _registerForm.value = FormState.ERR_PASSWORD
        } else {
            _registerForm.value = FormState.VALID
        }
    }
}