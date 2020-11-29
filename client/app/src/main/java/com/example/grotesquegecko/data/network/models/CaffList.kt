package com.example.grotesquegecko.data.network.models

data class CaffList(
    val caffs: MutableList<Caff>,
    val offset: Int,
    val pageSize: Int,
    val totalCount: Int
)