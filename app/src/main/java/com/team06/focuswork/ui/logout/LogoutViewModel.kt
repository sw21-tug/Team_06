package com.team06.focuswork.ui.logout

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team06.focuswork.R
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.data.Result
import kotlinx.coroutines.launch

class LogoutViewModel : ViewModel() {
    private val loginRepository = LoginRepository

    fun logout() {
        loginRepository.logout()
    }
}