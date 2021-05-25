package com.team06.focuswork.espressoUtil

import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.model.LoggedInUser
import io.mockk.every
import io.mockk.mockkObject

object MockUtil {
    fun mockUser(user: LoggedInUser) {
        // Mock Test User
        mockkObject(LoginRepository)
        every { LoginRepository.getUser() } answers { user }
    }
}