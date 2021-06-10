package com.team06.focuswork.ui.logout

import androidx.lifecycle.ViewModel
import com.team06.focuswork.data.LoginRepository

class LogoutViewModel : ViewModel() {
    fun logout() {
        LoginRepository.logout()
    }
}