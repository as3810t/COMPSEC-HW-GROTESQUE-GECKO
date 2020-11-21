package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor(
    private val grotesqueGeckoAPI: GrotesqueGeckoAPI
) {

    suspend fun registerUser(email: String, password: String, username: String): Boolean {
        //TODO save token
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        return grotesqueGeckoAPI.register(requestBody).await().isSuccessful
    }

    suspend fun logInUser(email: String, password: String, username: String): Boolean {
        //TODO save token
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        return grotesqueGeckoAPI.login(requestBody).await().isSuccessful
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