package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {

    suspend fun registerUser(email: String, password: String, username: String): Boolean {
        return networkDataSource.registerUser(email, password, username)
    }

    suspend fun logInUser(email: String, password: String, username: String): Boolean {
        return networkDataSource.logInUser(email, password, username)
    }
}