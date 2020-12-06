package com.example.grotesquegecko.ui.allusers

sealed class AllUsersViewState

object Loading : AllUsersViewState()

object AllUsersReady : AllUsersViewState()
