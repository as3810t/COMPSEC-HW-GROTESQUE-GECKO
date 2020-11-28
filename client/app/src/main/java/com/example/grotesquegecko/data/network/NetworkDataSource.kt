package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.CaffPreview
import com.example.grotesquegecko.data.network.models.LoginData
import com.example.grotesquegecko.data.network.token.Token
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor(
    private val grotesqueGeckoAPI: GrotesqueGeckoAPI,
    private val token: Token
) {

    suspend fun registerUser(
        email: String,
        password: String,
        username: String
    ): retrofit2.Response<LoginData> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        return grotesqueGeckoAPI.register(requestBody).await()
    }

    suspend fun logInUser(
        email: String,
        password: String,
        username: String
    ): retrofit2.Response<LoginData> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        return grotesqueGeckoAPI.login(requestBody).await()
    }

    suspend fun logout(): Boolean {
        return if (token.hasToken()) {
            val response = grotesqueGeckoAPI.logout(auth = token.getToken()!!).await()
            token.deleteToken()
            response.code() == 200
        } else true
    }

    suspend fun getCaffList(): MutableList<CaffPreview> {
        if (!token.hasToken()) {
            return mutableListOf()
        }

        val response = grotesqueGeckoAPI.getAllCaffs(
            auth = token.getToken()!!,
            offset = null,
            pageSize = null,
            tag = "",
            title = "",
            userId = ""
        ).await()

        val caffList = mutableListOf<CaffPreview>()

        if (response.body() != null) {
            for (caff in response.body()!!.caffs) {
                caffList.add(
                    CaffPreview(
                        caff.id,
                        caff.title
                    )
                )
            }
        }
        return caffList
    }

    suspend fun getCommentList(id: String): MutableList<CaffComment> {
        if (!token.hasToken()) {
            return mutableListOf()
        }

        val response = grotesqueGeckoAPI.getAllComments(
            auth = token.getToken()!!,
            id = id,
            offset = null,
            pageSize = null
        ).await()

        val commentList = mutableListOf<CaffComment>()

        if (response.body() != null) {
            for (comment in response.body()!!.comments) {
                commentList.add(
                    CaffComment(
                        comment.caffId,
                        comment.content,
                        comment.createdDate,
                        comment.id,
                        comment.lastModifiedById,
                        comment.lastModifiedByName,
                        comment.lastModifiedDate,
                        comment.userId,
                        comment.userName
                    )
                )
            }
        }
        return commentList
    }

    suspend fun createComment(
            content: String,
            id: String
    ): retrofit2.Response<CaffComment> {
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .build()
        return grotesqueGeckoAPI.createComment(
                auth = "Bearer ${token.getToken()!!}",
                requestBody,
                id = id
        ).await()
    }
}