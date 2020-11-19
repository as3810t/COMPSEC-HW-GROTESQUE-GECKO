package com.example.grotesquegecko.ui.login

sealed class LoginViewState

object Login : LoginViewState()
object Register : LoginViewState()

object LoadingLogin : LoginViewState()
object LoadingRegistration : LoginViewState()

object LoginFailed : LoginViewState()
object RegisterFailed : LoginViewState()

data class UserLoaded(val loggedIn: Boolean) : LoginViewState()
