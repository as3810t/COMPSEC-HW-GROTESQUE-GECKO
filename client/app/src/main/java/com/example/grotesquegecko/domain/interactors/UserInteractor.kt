package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import com.example.grotesquegecko.data.network.models.UserData
import com.example.grotesquegecko.data.network.models.UserRole
import com.example.grotesquegecko.data.network.token.Token
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val token: Token,
    private val userRole: UserRole
) {

    suspend fun registerUser(email: String, password: String, username: String): Boolean {
        val response = networkDataSource.registerUser(email, password, username)
        return if (response.code() == 200 && response.body() != null) {
            token.saveToken(response.body()!!.token)
            userRole.saveUserRole(isUser = true)
            for (role in response.body()!!.roles) {
                if (role == "ROLE_ADMIN") {
                    userRole.saveUserRole(isUser = false)
                }
            }
            true
        } else false
    }

    suspend fun logInUser(email: String, password: String, username: String): Boolean {
        val response = networkDataSource.logInUser(email, password, username)
        return if (response.code() == 200 && response.body() != null) {
            token.saveToken(response.body()!!.token)
            userRole.saveUserRole(isUser = true)
            for (role in response.body()!!.roles) {
                if (role == "ROLE_ADMIN") {
                    userRole.saveUserRole(isUser = false)
                }
            }
            true
        } else false
    }

    suspend fun logout(): Boolean {
        return networkDataSource.logout()
    }

    suspend fun forgottenPassword(email: String, username: String): Boolean {
        return networkDataSource.forgottenPassword(
            email = email,
            username = username
        )
    }

    suspend fun editUserData(email: String, password: String, username: String): Boolean {
        return networkDataSource.editUserData(
            email = email,
            password = password,
            username = username
        )
    }

    suspend fun editUserData(
        email: String,
        password: String,
        username: String,
        id: String
    ): Boolean {
        return networkDataSource.editUserData(
            email = email,
            password = password,
            username = username,
            id = id
        )
    }

    fun myAccountIsUser(): Boolean {
        if (!userRole.hasUserRole()) {
            return false
        }
        return userRole.getUserRole()!!
    }

    suspend fun getUserData(): UserData? {
        return networkDataSource.getMe()
    }

    suspend fun getAllUsers(): MutableList<UserData> {
        return networkDataSource.getAllUsers()
    }

    suspend fun deleteUser(userId: String) {
        networkDataSource.deleteUser(userId)
    }
}