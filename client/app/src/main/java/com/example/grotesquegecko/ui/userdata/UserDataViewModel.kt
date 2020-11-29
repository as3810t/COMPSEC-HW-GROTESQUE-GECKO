package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class UserDataViewModel @Inject constructor(
    private val userDataPresenter: UserDataPresenter
) : RainbowCakeViewModel<UserDataViewState>(Loading) {

    fun load() = execute {
        viewState = UserDataReady(userDataPresenter.getData())
    }

}
