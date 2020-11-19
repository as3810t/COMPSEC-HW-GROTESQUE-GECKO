package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {

    suspend fun registerUser(email: String, username: String, password: String): Boolean {
        return networkDataSource.registerUser(email, username, password)
    }

    suspend fun logInUser(emailOrUsername: String, password: String): Boolean {
        return networkDataSource.logInUser(emailOrUsername, password)
    }
}