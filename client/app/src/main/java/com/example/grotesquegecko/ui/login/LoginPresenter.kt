package com.example.grotesquegecko.ui.login

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun registerUser(email: String, password: String, username: String): Boolean =
        withIOContext {
            return@withIOContext userInteractor.registerUser(email, password, username)
        }

    suspend fun logInUser(emailOrUsername: String, password: String, username: String): Boolean =
        withIOContext {
            return@withIOContext userInteractor.logInUser(emailOrUsername, password, username)
        }

    suspend fun forgottenPassword(email: String, username: String): Boolean = withIOContext {
        userInteractor.forgottenPassword(
            email = email,
            username = username
        )
    }
}
