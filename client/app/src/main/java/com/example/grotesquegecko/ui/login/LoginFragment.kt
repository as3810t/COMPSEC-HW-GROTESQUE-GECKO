package com.example.grotesquegecko.ui.login

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Patterns
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.example.grotesquegecko.MainActivity
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.login.LoginFragment.Constants.FADE_DURATION
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_login.*
import kotlinx.android.synthetic.main.fragment_login_register.*

class LoginFragment : RainbowCakeFragment<LoginViewState, LoginViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_login

    private object Flipper {
        const val LOGIN = 0
        const val REGISTER = 1
    }

    private object Constants {
        const val FADE_DURATION = 200L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    override fun render(viewState: LoginViewState) {
        TransitionManager.beginDelayedTransition(
            login_flipper,
            AutoTransition().setDuration(FADE_DURATION)
        )
        when (viewState) {
            is Login -> logIn()
            is Register -> register()
            is LoadingLogin -> showLoadingLogin()
            is LoadingRegistration -> showLoadingRegistration()
            is LoginFailed -> showLoginFailed()
            is RegisterFailed -> showRegisterFailed()
            is UserLoaded -> userLoaded()
        }.exhaustive
    }

    private fun showRegisterFailed() {
        registrationProgressBar.visibility = View.GONE
        textViewRegisterFailed.visibility = View.GONE
        login_flipper.displayedChild = Flipper.REGISTER
    }

    private fun showLoginFailed() {
        loginProgressBar.visibility = View.GONE
        textViewSignInFailed.visibility = View.VISIBLE
        login_flipper.displayedChild = Flipper.LOGIN
    }

    private fun logIn() {
        loginProgressBar.visibility = View.GONE
        textViewSignInFailed.visibility = View.GONE
        login_flipper.displayedChild = Flipper.LOGIN
    }

    private fun register() {
        registrationProgressBar.visibility = View.GONE
        textViewRegisterFailed.visibility = View.GONE
        login_flipper.displayedChild = Flipper.REGISTER
    }

    private fun showLoadingLogin() {
        loginProgressBar.visibility = View.VISIBLE
    }

    private fun showLoadingRegistration() {
        registrationProgressBar.visibility = View.VISIBLE
    }

    private fun userLoaded() {
        loginProgressBar.visibility = View.GONE
        registrationProgressBar.visibility = View.GONE
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun setupButtons() {
        setupLoginButton()
        setupRegisterButton()
        setupSwitchToRegisterButton()
        setupSwitchToLoginButton()
    }

    private fun setupSwitchToLoginButton() {
        textViewSignUp.setOnClickListener {
            login_flipper.displayedChild =
                Flipper.LOGIN
        }
    }

    private fun setupSwitchToRegisterButton() {
        textViewSignIn.setOnClickListener {
            login_flipper.displayedChild =
                Flipper.REGISTER
        }
    }

    private fun setupRegisterButton() {
        btnRegistration.setOnClickListener {
            val email: String = registerEditTextEmail.text.toString().trim()
            val username: String = registerEditTextUsername.text.toString().trim()
            val password: String = registerEditTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                registerEditTextEmail.error = getString(R.string.login_fragment_email_required)
                registerEditTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                registerEditTextEmail.error = getString(R.string.login_fragment_enter_valid_email)
                registerEditTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                registerEditTextUsername.error =
                    getString(R.string.login_fragment_username_required)
                registerEditTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (username.length < 5) {
                registerEditTextUsername.error =
                    getString(R.string.login_fragment_username_minimum_length)
                registerEditTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                registerEditTextPassword.error =
                    getString(R.string.login_fragment_password_required)
                registerEditTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                registerEditTextPassword.error =
                    getString(R.string.login_fragment_password_minimum_length)
                registerEditTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != registerEditTextPasswordAgain.text.toString().trim()) {
                registerEditTextPassword.error =
                    getString(R.string.login_fragment_passwords_equality)
                registerEditTextPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.registerUser(email, username, password)
        }
    }

    private fun setupLoginButton() {
        buttonLogIn.setOnClickListener {
            val emailOrUsername: String = logInEditTextEmailOrUserName.text.toString().trim()
            val password: String = logInEditTextPassword.text.toString().trim()

            if (emailOrUsername.isEmpty()) {
                logInEditTextEmailOrUserName.error =
                    getString(R.string.login_fragment_email_required)
                logInEditTextEmailOrUserName.requestFocus()
                return@setOnClickListener
            }

            if (emailOrUsername.length < 5) {
                logInEditTextEmailOrUserName.error =
                    getString(R.string.login_fragment_username_minimum_length)
                logInEditTextEmailOrUserName.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                logInEditTextPassword.error = getString(R.string.login_fragment_password_required)
                logInEditTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                logInEditTextPassword.error =
                    getString(R.string.login_fragment_password_minimum_length)
                logInEditTextPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.logInUser(emailOrUsername, password)
        }
    }
}
