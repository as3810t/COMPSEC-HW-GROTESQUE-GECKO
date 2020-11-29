package com.example.grotesquegecko.ui.userdata

sealed class UserDataViewState

object Loading : UserDataViewState()

data class UserDataReady(val data: String = "") : UserDataViewState()
