package com.example.grotesquegecko.data.network.token

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Token @Inject constructor() {
    private var token: String? = null

    fun getToken(): String? = token

    fun saveToken(token: String?) {
        this.token = token
    }

    fun deleteToken() {
        token = null
    }

    fun hasToken(): Boolean {
        return token != null
    }
}