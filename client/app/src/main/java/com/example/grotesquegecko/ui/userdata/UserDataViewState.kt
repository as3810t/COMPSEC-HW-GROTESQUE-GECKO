package com.example.grotesquegecko.ui.userdata

sealed class UserDataViewState

object Loading : UserDataViewState()
object User : UserDataViewState()
object Admin : UserDataViewState()


