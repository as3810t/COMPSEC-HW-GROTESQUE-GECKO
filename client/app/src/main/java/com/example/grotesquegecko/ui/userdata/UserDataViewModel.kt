package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class UserDataViewModel @Inject constructor(
    private val userDataPresenter: UserDataPresenter
) : RainbowCakeViewModel<UserDataViewState>(Loading) {

    object Logout : OneShotEvent

    fun load() = execute {
        viewState = UserDataReady(userDataPresenter.getData())
    }

    fun logout() = execute {
        if (userDataPresenter.logout()) {
            postEvent(Logout)
        }
    }
}
