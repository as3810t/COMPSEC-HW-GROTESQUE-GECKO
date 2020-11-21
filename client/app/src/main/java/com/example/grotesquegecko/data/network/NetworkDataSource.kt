package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor() {

    suspend fun registerUser(email: String, username: String, password: String): Boolean {
        //TODO send network request
        return true
    }

    suspend fun logInUser(emailOrUsername: String, password: String): Boolean {
        //TODO send network request
        return true
    }

    suspend fun getCaffList(): MutableList<CaffPreview> {
        //TODO send network request
        return mutableListOf(
            CaffPreview(
                "1",
                "Grotesque",
                "https://scx2.b-cdn.net/gfx/news/2017/2-5-universityof.jpg"
            ),
            CaffPreview(
                "2",
                "Gecko",
                "https://i.insider.com/53bdb318ecad04c707b61b17?width=700&format=jpeg&auto=webp"
            )
        )
    }
}