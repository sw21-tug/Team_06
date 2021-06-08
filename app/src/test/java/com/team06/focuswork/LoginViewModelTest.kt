package com.team06.focuswork

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.data.Result
import com.team06.focuswork.model.LoggedInUser
import com.team06.focuswork.ui.login.LoginViewModel
import io.mockk.every
import io.mockk.mockkObject
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginViewModelTest {

    private val firstname = "Max"
    private val lastname = "Mustermann"
    private val username = "test@mail.com"
    private val password = "password"
    private lateinit var viewModel: LoginViewModel

    @Rule
    @JvmField //needed for LiveData to work synchronously
    var rule: TestRule? = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    lateinit var resultSucces: Result.Success<LoggedInUser>
    lateinit var resultError: Result.Error

    @Before
    fun init() {
        mockkObject(LoginRepository)
        viewModel = LoginViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun registerDataChangedTest() {
        assertNull(viewModel.registerFormState.value)

        viewModel.registerDataChanged(firstname, lastname, username, password)
        assertEquals(LoginViewModel.FormState.VALID, viewModel.registerFormState.value)

        viewModel.registerDataChanged("", lastname, username, password)
        assertEquals(LoginViewModel.FormState.ERR_FIRSTNAME, viewModel.registerFormState.value)

        viewModel.registerDataChanged(firstname, "", username, password)
        assertEquals(LoginViewModel.FormState.ERR_LASTNAME, viewModel.registerFormState.value)

        viewModel.registerDataChanged(firstname, lastname, "sdfs", password)
        assertEquals(LoginViewModel.FormState.ERR_USERNAME, viewModel.registerFormState.value)

        viewModel.registerDataChanged(firstname, lastname, username, "pas")
        assertEquals(LoginViewModel.FormState.ERR_PASSWORD, viewModel.registerFormState.value)
    }

    @Test
    fun loginDataChangedTest() {
        assertNull(viewModel.loginFormState.value)

        viewModel.loginDataChanged(username, password)
        assertEquals(LoginViewModel.FormState.VALID, viewModel.loginFormState.value)

        viewModel.loginDataChanged("", password)
        assertEquals(LoginViewModel.FormState.ERR_USERNAME, viewModel.loginFormState.value)

        viewModel.loginDataChanged(username, "pas")
        assertEquals(LoginViewModel.FormState.ERR_PASSWORD, viewModel.loginFormState.value)
    }

    @Test
    fun loginSuccessTest(): Unit = testDispatcher.runBlockingTest {
        resultSucces = Result.Success(LoggedInUser("hash"))
        every { LoginRepository.login(username, password) } returns resultSucces

        viewModel.login(username, password)
        assertEquals(LoginViewModel.LoginState.SUCCESS, viewModel.loginResult.value)
    }

    @Test
    fun loginErrorTest(): Unit = testDispatcher.runBlockingTest {
        resultError = Result.Error(Exception("Message"))
        every {
            LoginRepository.login(username, password)
        } returns resultError

        viewModel.login(username, password)
        assertEquals(LoginViewModel.LoginState.ERROR, viewModel.loginResult.value)
    }

    @Test
    fun registerSuccessTest(): Unit = testDispatcher.runBlockingTest {
        resultSucces = Result.Success(LoggedInUser("hash"))
        every {
            LoginRepository.register(firstname, lastname, username, password)
        } returns resultSucces

        viewModel.register(firstname, lastname, username, password)
        assertEquals(LoginViewModel.LoginState.SUCCESS, viewModel.loginResult.value)
    }

    @Test
    fun registerErrorTest(): Unit = testDispatcher.runBlockingTest {
        resultError = Result.Error(Exception("Message"))
        every {
            LoginRepository.register(firstname, lastname, username, password)
        } returns resultError

        viewModel.register(firstname, lastname, username, password)
        assertEquals(LoginViewModel.LoginState.ERROR, viewModel.loginResult.value)
    }
}