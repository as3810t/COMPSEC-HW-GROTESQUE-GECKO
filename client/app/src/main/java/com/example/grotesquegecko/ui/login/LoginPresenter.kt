package com.example.grotesquegecko.ui.login

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class LoginPresenter @Inject constructor() {

    suspend fun registerUser(email: String, password: String): Boolean = withIOContext{
        //TODO get data from user interactor
        return@withIOContext true
    }

    suspend fun logInUser(email: String, password: String): Boolean = withIOContext{
        //TODO get data from user interactor
        return@withIOContext true
    }

}
