package com.team06.focuswork.ui.logout

import androidx.lifecycle.ViewModel
import com.team06.focuswork.data.LoginRepository

class LogoutViewModel : ViewModel() {
    private val loginRepository = LoginRepository

    fun logout() {
        loginRepository.logout()
    }
}