package com.example.grotesquegecko.domain.interactors

import javax.inject.Inject

class UserInteractor @Inject constructor() {

    suspend fun registerUser(email: String, username: String, password: String): Boolean {
        //TODO get data from network data source
        return true
    }

    suspend fun logInUser(emailOrUsername: String, password: String): Boolean {
        //TODO get data from network data source
        return true
    }
}