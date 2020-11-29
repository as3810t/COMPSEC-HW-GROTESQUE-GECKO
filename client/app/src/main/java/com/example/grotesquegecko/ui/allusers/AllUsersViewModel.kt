package com.example.grotesquegecko.ui.allusers

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class AllUsersViewModel @Inject constructor(
    private val allUsersPresenter: AllUsersPresenter
) : RainbowCakeViewModel<AllUsersViewState>(Loading) {

    fun load() = execute {
        viewState = AllUsersReady(allUsersPresenter.getAllUsers())
    }

    fun editUserData(email: String, password: String, username: String, id: String) = execute {
        allUsersPresenter.editUserData(
            email = email,
            password = password,
            username = username,
            id = id
        )
    }

    fun deleteUser(userId: String) = execute {
        allUsersPresenter.deleteUser(userId)
    }
}
