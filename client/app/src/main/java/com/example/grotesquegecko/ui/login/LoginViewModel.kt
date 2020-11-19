package com.example.grotesquegecko.ui.login

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginPresenter: LoginPresenter
) : RainbowCakeViewModel<LoginViewState>(Login) {

    fun registerUser(email: String, username: String, password: String) = execute {
        viewState = LoadingRegistration
        val successful = loginPresenter.registerUser(email, username, password)
        viewState = if (successful) {
            UserLoaded(successful)
        } else {
            RegisterFailed
        }
    }

    fun logInUser(emailOrUsername: String, password: String) = execute {
        viewState = LoadingLogin
        val successful = loginPresenter.logInUser(emailOrUsername, password)
        viewState = if (successful) {
            UserLoaded(successful)
        } else {
            LoginFailed
        }
    }

}
