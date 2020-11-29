package com.example.grotesquegecko.ui.login

import android.util.Patterns
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginPresenter: LoginPresenter
) : RainbowCakeViewModel<LoginViewState>(Login) {

    data class PasswordResetWasSuccessful(
        var passwordResetWasSuccessful: Boolean
    ) : OneShotEvent

    fun registerUser(email: String, password: String, username: String) = execute {
        viewState = LoadingRegistration
        val successful = loginPresenter.registerUser(email, password, username)
        viewState = if (successful) {
            UserLoaded(successful)
        } else {
            RegisterFailed
        }
    }

    fun logInUser(emailOrUsername: String, password: String) = execute {
        viewState = LoadingLogin
        val successful = if (Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
            loginPresenter.logInUser(emailOrUsername, password, "")
        } else {
            loginPresenter.logInUser("", password, emailOrUsername)
        }
        viewState = if (successful) {
            UserLoaded(successful)
        } else {
            LoginFailed
        }
    }

    fun forgottenPassword(email: String, username: String) = execute {
        if (loginPresenter.forgottenPassword(
                email = email,
                username = username
            )
        ) {
            postEvent(PasswordResetWasSuccessful(true))
        } else {
            postEvent(PasswordResetWasSuccessful(false))
        }
    }
}
