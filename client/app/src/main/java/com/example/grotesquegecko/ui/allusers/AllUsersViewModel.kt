package com.example.grotesquegecko.ui.allusers

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.example.grotesquegecko.data.network.models.UserData
import javax.inject.Inject

class AllUsersViewModel @Inject constructor(
    private val allUsersPresenter: AllUsersPresenter
) : RainbowCakeViewModel<AllUsersViewState>(Loading) {

    data class UserDatas(
        val users: MutableList<UserData>
    ) : OneShotEvent

    fun load() = execute {
        postEvent(UserDatas(allUsersPresenter.getAllUsers()))
        viewState = AllUsersReady
    }

    fun editUserData(email: String, password: String, username: String, id: String) = execute {
        allUsersPresenter.editUserData(
            email = email,
            password = password,
            username = username,
            id = id
        )
        load()
    }

    fun deleteUser(userId: String) = execute {
        allUsersPresenter.deleteUser(userId)
        load()
    }
}
