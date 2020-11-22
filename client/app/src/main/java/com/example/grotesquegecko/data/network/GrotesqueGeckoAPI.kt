package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.data.network.models.CaffList
import com.example.grotesquegecko.data.network.models.LoginData
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @Headers("accept: application/json")
    @GET("/caff")
    fun getAllCaffs(
        @Header("Authorization") auth: String,
        @Query("offset") offset: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("tag") tag: String?,
        @Query("title") title: String?,
        @Query("userId") userId: String?
    ): Deferred<Response<CaffList>>
}