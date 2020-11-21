package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.data.network.models.LoginData
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrotesqueGeckoAPI {

    @Headers("accept: application/json")
    @POST("/auth/login")
    fun login(
        @Body body: RequestBody
    ): Deferred<Response<LoginData>>

    @Headers("accept: application/json")
    @POST("/auth/register")
    fun register(
        @Body body: RequestBody
    ): Deferred<Response<LoginData>>
}