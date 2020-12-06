package com.example.grotesquegecko.ui.allusers

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.UserData
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class AllUsersPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun getAllUsers(): MutableList<UserData> = withIOContext {
        return@withIOContext userInteractor.getAllUsers()
    }


    suspend fun editUserData(email: String, password: String, username: String, id: String) =
        withIOContext {
            return@withIOContext userInteractor.editUserData(
                email = email,
                password = password,
                username = username,
                id = id
            )
        }

    suspend fun deleteUser(userId: String) = withIOContext {
        userInteractor.deleteUser(userId)
    }
}
