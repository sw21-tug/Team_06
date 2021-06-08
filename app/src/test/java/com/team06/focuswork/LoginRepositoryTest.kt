package com.team06.focuswork

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team06.focuswork.data.FireBaseFireStoreUtil
import com.team06.focuswork.data.LoginDataSource
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.data.Result
import com.team06.focuswork.model.LoggedInUser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox


@RunWith(PowerMockRunner::class)
@PrepareForTest(LoginRepository::class, FirebaseFirestore::class, Log::class)
class LoginRepositoryTest {

    private val firstname = "Max"
    private val lastname = "Mustermann"
    private val username = "test@mail.com"
    private val password = "password"

    @Mock
    private var fireStoreUtilMock: FireBaseFireStoreUtil? = null

    @Mock
    private var fireStoreMock: FirebaseFirestore? = null

    private var loginDataSourceSpy: LoginDataSource? = null

    @Before
    fun init() {
        PowerMockito.mockStatic(Log::class.java)
        fireStoreUtilMock = PowerMockito.mock(FireBaseFireStoreUtil::class.java)
        PowerMockito.mockStatic(FirebaseFirestore::class.java)
        fireStoreMock = Mockito.mock(FirebaseFirestore::class.java)
        PowerMockito.`when`(FirebaseFirestore.getInstance()).thenReturn(fireStoreMock)

        PowerMockito.mockStatic(LoginRepository::class.java)
        val loginDataSource = LoginDataSource()
        loginDataSourceSpy = Mockito.spy(loginDataSource)

        Whitebox.setInternalState(LoginRepository::class.java, "dataSource", loginDataSourceSpy)
        Whitebox.setInternalState(loginDataSourceSpy, "fireStoreUtil", fireStoreUtilMock)

        LoginRepository.user = null
    }

    @Test
    fun logInUserSuccessTest() {
        Mockito.`when`(fireStoreUtilMock?.retrieveUser(username, password))
            .thenReturn((LoggedInUser("hash")))

        LoginRepository.login(username, password)
        val result = loginDataSourceSpy?.login(username, password)

        assert(LoginRepository.user != null)
        assert(result is Result.Success)
    }

    @Test
    fun logoutUserTest() {
        Mockito.`when`(fireStoreUtilMock?.retrieveUser(username, password))
            .thenReturn((LoggedInUser("hash")))

        LoginRepository.login(username, password)
        LoginRepository.logout()

        assert(LoginRepository.user == null)
    }

    @Test
    fun logInUserErrorTest() {
        Mockito.`when`(fireStoreUtilMock?.retrieveUser(username, password))
            .thenAnswer { throw Throwable() }

        LoginRepository.login(username, password)
        val result = loginDataSourceSpy?.login(username, password)

        assert(LoginRepository.user == null)
        assert(result is Result.Error)
    }

    @Test
    fun registerUserSuccessTest() {
        Mockito.`when`(fireStoreUtilMock?.addUser(firstname, lastname, username, password))
            .thenReturn((LoggedInUser("hash")))

        LoginRepository.register(firstname, lastname, username, password)
        val result = loginDataSourceSpy?.register(firstname, lastname, username, password)

        assert(LoginRepository.user != null)
        assert(result is Result.Success)
    }

    @Test
    fun registerUserErrorTest() {
        Mockito.`when`(fireStoreUtilMock?.addUser(firstname, lastname, username, password))
            .thenAnswer { throw Throwable() }

        LoginRepository.register(firstname, lastname, username, password)
        val result = loginDataSourceSpy?.register(firstname, lastname, username, password)

        assert(LoginRepository.user == null)
        assert(result is Result.Error)
    }
}
