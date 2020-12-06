package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.data.network.models.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    @POST("/auth/logout")
    fun logout(
        @Header("Authorization") auth: String
    ): Deferred<Response<Void>>

    @Headers("accept: application/json")
    @POST("/auth/passwordReset")
    fun passwordReset(
        @Body body: RequestBody
    ): Deferred<Response<Void>>

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

    @Headers("accept: application/json")
    @GET("/caff/{id}/comment")
    fun getAllComments(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Query("offset") offset: Int?,
        @Query("pageSize") pageSize: Int?
    ): Deferred<Response<CommentList>>

    @Headers("accept: application/json")
    @POST("/caff/{id}/comment")
    fun createComment(
        @Header("Authorization") auth: String,
        @Body body: RequestBody,
        @Path("id") id: String
    ): Deferred<Response<CaffComment>>

    @Headers("accept: application/octet-stream")
    @GET("/caff/{id}/download")
    fun downloadCaff(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Deferred<Response<ResponseBody>>

    @Headers("accept: application/json")
    @PUT("/user/{id}")
    fun editUserData(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Body body: RequestBody
    ): Deferred<Response<UserData>>

    @Headers("accept: application/json")
    @GET("/user/me")
    fun getMe(
        @Header("Authorization") auth: String
    ): Deferred<Response<UserData>>

    @Headers("accept: application/json")
    @GET("/user")
    fun getAllUsers(
        @Header("Authorization") auth: String,
        @Query("offset") offset: Int?,
        @Query("pageSize") pageSize: Int?
    ): Deferred<Response<AllUsers>>

    @Headers("accept: application/json")
    @DELETE("/user/{id}")
    fun deleteUser(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Deferred<Response<UserData>>
}