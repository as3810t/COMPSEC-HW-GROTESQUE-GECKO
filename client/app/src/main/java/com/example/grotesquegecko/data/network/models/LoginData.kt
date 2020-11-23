package com.example.grotesquegecko.data.network.models

data class LoginData(
    val roles: List<String>,
    val token: String,
    val username: String
)