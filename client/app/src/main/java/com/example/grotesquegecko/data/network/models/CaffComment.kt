package com.example.grotesquegecko.data.network.models

data class CaffComment(
    val caffId: String,
    val content: String?,
    val createdDate: String,
    val id: String,
    val lastModifiedById: String,
    val lastModifiedByName: String,
    val lastModifiedDate: String,
    val userId: String,
    val userName: String
)