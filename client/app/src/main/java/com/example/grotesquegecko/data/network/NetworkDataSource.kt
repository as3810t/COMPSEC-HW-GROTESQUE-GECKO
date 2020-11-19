package com.example.grotesquegecko.data.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor() {

    suspend fun registerUser(email: String, username: String, password: String): Boolean {
        //TODO send network request
        return true
    }

    suspend fun logInUser(emailOrUsername: String, password: String): Boolean {
        //TODO send network request
        return true
    }
}