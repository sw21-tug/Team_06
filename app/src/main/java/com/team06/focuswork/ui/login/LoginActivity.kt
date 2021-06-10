package com.team06.focuswork.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R
import com.team06.focuswork.ThemedAppCompatActivity
import com.team06.focuswork.data.LoginDataSource
import com.team06.focuswork.data.LoginRepository
import com.team06.focuswork.databinding.ActivityLoginBinding
import com.team06.focuswork.ui.util.SnackBarUtil

class LoginActivity : ThemedAppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginRepository.dataSource = LoginDataSource()
        bindComponents()
        setUpLoginFormState()
        setUpLoginResult()
        setUpTextListeners()
        setUpSubmitButtons()
        autoLogin()
    }

    private fun autoLogin(): Boolean {
        val user = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString("USER", null)
        val pass = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString("PASS", null)

        if (!user.isNullOrEmpty() && !pass.isNullOrEmpty()) {
            loading.visibility = View.VISIBLE
            loginViewModel.login(user, pass)
            return true
        }

        return false
    }

    private fun bindComponents() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        username = binding.username
        password = binding.password
        login = binding.login
        register = binding.register
        loading = binding.loading
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }

    private fun setUpSubmitButtons() {
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login(username.text.toString(), password.text.toString())
        }

        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpTextListeners() {
        username.afterTextChanged {
            loginViewModel.loginDataChanged(username.text.toString(), password.text.toString())
        }

        password.afterTextChanged {
            loginViewModel.loginDataChanged(username.text.toString(), password.text.toString())
        }

        password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    loginViewModel.login(username.text.toString(), password.text.toString())
            }
            false
        }
    }

    private fun setUpLoginFormState() {
        loginViewModel.loginFormState.observe(this@LoginActivity, { loginState ->
            // disable login button unless both username / password is valid
            login.isEnabled = loginState == LoginViewModel.FormState.VALID
            if (loginState == LoginViewModel.FormState.ERR_USERNAME) {
                username.error = getString(R.string.invalid_username)
            }
            if (loginState == LoginViewModel.FormState.ERR_PASSWORD) {
                password.error = getString(R.string.invalid_password)
            }
        })
    }

    private fun setUpLoginResult() {
        loginViewModel.loginResult.observe(this@LoginActivity, { loginResult ->
            loading.visibility = View.GONE
            when (loginResult) {
                LoginViewModel.LoginState.SUCCESS -> updateUiWithUser()
                else -> SnackBarUtil.showSnackBar(binding.root, R.string.login_failed, this)
            }
        })
    }

    private fun updateUiWithUser() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        setResult(Activity.RESULT_OK)

        if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()
        ) {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                .putString("USER", username.text.toString()).apply()
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                .putString("PASS", password.text.toString()).apply()
        }
        //Complete and destroy login activity once successful
        finish()
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}