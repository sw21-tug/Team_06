package com.team06.focuswork.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.data.Result

import com.team06.focuswork.R
import com.team06.focuswork.model.TasksViewModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch { // can be launched in a separate asynchronous job
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value = LoginResult(success = result.data)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun register(username: String, password: String) {
        viewModelScope.launch { // can be launched in a separate asynchronous job
            val result = loginRepository.register(username, password)

            if (result is Result.Success) {
                _loginResult.value = LoginResult(success = result.data)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }
}