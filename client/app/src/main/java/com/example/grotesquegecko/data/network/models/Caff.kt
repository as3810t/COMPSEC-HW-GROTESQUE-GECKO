package com.example.grotesquegecko.data.network.models

data class Caff(
    val id: String,
    val lastModifiedById: String,
    val lastModifiedByName: String,
    val lastModifiedDate: String,
    val ownerId: String,
    val ownerName: String,
    val tags: MutableList<String>,
    val title: String
)