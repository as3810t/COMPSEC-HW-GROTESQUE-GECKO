package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class UserDataPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun getData(): String = withIOContext {
        ""
    }

    suspend fun logout(): Boolean {
        return userInteractor.logout()
    }
}
