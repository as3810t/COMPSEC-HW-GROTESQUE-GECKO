package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class UserDataViewModel @Inject constructor(
    private val userDataPresenter: UserDataPresenter
) : RainbowCakeViewModel<UserDataViewState>(Loading) {

    object Logout : OneShotEvent
    object EditWasSuccessful : OneShotEvent
    object EditWasNotSuccessful : OneShotEvent

    fun load() = execute {
        viewState = UserDataReady(userDataPresenter.getData())
    }

    fun logout() = execute {
        if (userDataPresenter.logout()) {
            postEvent(Logout)
        }
    }

    fun editUserData(email: String, password: String, username: String) = execute {
        if (userDataPresenter.editUserData(
                email = email,
                password = password,
                username = username
            )
        ) {
            postEvent(EditWasSuccessful)
        } else {
            postEvent(EditWasNotSuccessful)
        }

    }
}
