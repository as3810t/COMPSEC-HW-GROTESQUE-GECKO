package com.example.grotesquegecko.data.network.models

data class CommentList(
    val comments: MutableList<CaffComment>,
    val offset: Int,
    val pageSize: Int,
    val totalCount: Int
)