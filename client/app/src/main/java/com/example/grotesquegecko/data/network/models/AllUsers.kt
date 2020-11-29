package com.example.grotesquegecko.data.network.models

data class AllUsers(
    val offset: Int,
    val pageSize: Int,
    val totalCount: Int,
    val users: MutableList<UserData>
)