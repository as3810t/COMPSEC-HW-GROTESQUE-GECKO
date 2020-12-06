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
    object UserDataNotLoaded : OneShotEvent
    data class UserInformation(
        val username: String,
        val email: String
    ) : OneShotEvent

    fun load() = execute {
        val user = userDataPresenter.getUserData()
        if (user == null) {
            postEvent(UserDataNotLoaded)
        } else {
            postEvent(UserInformation(user.username, user.email))
        }
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

    fun myAccountIsUser() {
        viewState = if (!userDataPresenter.myAccountIsUser()) {
            Admin
        } else {
            User
        }
    }
}
