package com.team06.focuswork.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R
import com.team06.focuswork.ThemedAppCompatActivity
import com.team06.focuswork.databinding.ActivityRegisterBinding

class RegisterActivity : ThemedAppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindComponents()
        setUpRegisterFormState()
        setUpRegisterResult()
        setUpTextListeners()
        setUpSubmitButtons()
    }

    private fun bindComponents() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firstname = binding.firstname
        lastname = binding.lastname
        username = binding.username
        password = binding.password
        login = binding.login
        register = binding.register
        loading = binding.loading
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }

    private fun setUpSubmitButtons() {
        register.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.register(
                firstname.text.toString(), lastname.text.toString(),
                username.text.toString(), password.text.toString()
            )
        }

        login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpTextListeners() {
        username.afterTextChanged {
            loginViewModel.registerDataChanged(
                firstname.text.toString(), lastname.text.toString(),
                username.text.toString(), password.text.toString()
            )
        }
        password.afterTextChanged {
            loginViewModel.registerDataChanged(
                firstname.text.toString(), lastname.text.toString(),
                username.text.toString(), password.text.toString()
            )
        }
        password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    loginViewModel.register(
                        firstname.text.toString(), lastname.text.toString(),
                        username.text.toString(), password.text.toString()
                    )
            }
            false
        }
    }

    private fun setUpRegisterResult() {
        loginViewModel.loginResult.observe(this@RegisterActivity, { loginResult ->

            loading.visibility = View.GONE
            when (loginResult) {
                LoginViewModel.LoginState.ERROR -> showLoginFailed(R.string.login_failed)
                LoginViewModel.LoginState.SUCCESS -> updateUiWithUser()
            }
        })
    }

    private fun setUpRegisterFormState() {
        loginViewModel.registerFormState.observe(this@RegisterActivity, { registerState ->

            // disable login button unless both username / password is valid
            register.isEnabled = registerState == LoginViewModel.FormState.VALID
            if (registerState == LoginViewModel.FormState.ERR_USERNAME) {
                username.error = getString(R.string.invalid_username)
            }
            password.error = when (registerState) {
                LoginViewModel.FormState.ERR_PASSWORD -> getString(R.string.invalid_password)
                LoginViewModel.FormState.ERR_FIRSTNAME -> getString(R.string.invalid_firstname)
                LoginViewModel.FormState.ERR_LASTNAME -> getString(R.string.invalid_lastname)
                else -> return@observe
            }
        })
    }

    private fun updateUiWithUser() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        setResult(Activity.RESULT_OK)

        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            .putString("USER", username.text.toString()).apply()
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            .putString("PASS", password.text.toString()).apply()

        //Complete and destroy login activity once successful
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val mSnackbar: Snackbar = Snackbar.make(binding.root, resources.getString(errorString), Snackbar.LENGTH_LONG)
                .setBackgroundTint(ResourcesCompat.getColor(applicationContext.resources, R.color.primary_text, null))
                .setTextColor(ResourcesCompat.getColor(applicationContext.resources, R.color.white, null))

        val mView = mSnackbar.view
        val mTextView = mView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        mTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        mSnackbar.show()
    }
}
