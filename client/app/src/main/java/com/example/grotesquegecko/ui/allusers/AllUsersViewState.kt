package com.example.grotesquegecko.ui.allusers

import com.example.grotesquegecko.data.network.models.UserData

sealed class AllUsersViewState

object Loading : AllUsersViewState()

data class AllUsersReady(val allUsers: MutableList<UserData>) : AllUsersViewState()
