package com.example.grotesquegecko.ui.login

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun registerUser(email: String, username: String, password: String): Boolean = withIOContext {
            return@withIOContext userInteractor.registerUser(email, username, password)
        }

    suspend fun logInUser(emailOrUsername: String, password: String): Boolean = withIOContext {
        return@withIOContext userInteractor.logInUser(emailOrUsername, password)
    }

}