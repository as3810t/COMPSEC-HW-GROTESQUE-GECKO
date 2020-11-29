package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import com.example.grotesquegecko.data.network.token.Token
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val token: Token
) {

    suspend fun registerUser(email: String, password: String, username: String): Boolean {
        val response = networkDataSource.registerUser(email, password, username)
        return if (response.code() == 200 && response.body() != null) {
            token.saveToken(response.body()!!.token)
            true
        } else false
    }

    suspend fun logInUser(email: String, password: String, username: String): Boolean {
        val response = networkDataSource.logInUser(email, password, username)
        return if (response.code() == 200 && response.body() != null) {
            token.saveToken(response.body()!!.token)
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
}