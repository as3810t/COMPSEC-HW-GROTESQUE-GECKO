package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.UserData
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class UserDataPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun getUserData(): UserData? = withIOContext {
        return@withIOContext userInteractor.getUserData()
    }

    suspend fun logout(): Boolean {
        return userInteractor.logout()
    }

    suspend fun editUserData(email: String, password: String, username: String) = withIOContext {
        return@withIOContext userInteractor.editUserData(
            email = email,
            password = password,
            username = username
        )
    }

    fun myAccountIsUser(): Boolean {
        return userInteractor.myAccountIsUser()
    }
}
