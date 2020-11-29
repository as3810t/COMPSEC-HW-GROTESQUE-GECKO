package com.example.grotesquegecko.data.network.models

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRole @Inject constructor() {
    private var isUser: Boolean? = null

    fun getUserRole(): Boolean? = isUser

    fun saveUserRole(isUser: Boolean?) {
        this.isUser = isUser
    }

    fun deleteUserRole() {
        isUser = null
    }

    fun hasUserRole(): Boolean {
        return isUser != null
    }
}