package com.example.grotesquegecko.data.network

import android.net.Uri
import android.os.FileUtils
import com.example.grotesquegecko.data.network.models.*
import com.example.grotesquegecko.data.network.token.Token
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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
            val response = grotesqueGeckoAPI.logout(auth = "Bearer ${token.getToken()!!}").await()
            token.deleteToken()
            response.code() == 200
        } else true
    }

    suspend fun forgottenPassword(email: String, username: String): Boolean {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("username", username)
            .build()
        return grotesqueGeckoAPI.passwordReset(requestBody).await().code() == 200
    }

    suspend fun getCaffList(): MutableList<CaffPreview> {
        if (!token.hasToken()) {
            return mutableListOf()
        }

        val response = grotesqueGeckoAPI.getAllCaffs(
            auth = "Bearer ${token.getToken()!!}",
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
            auth = "Bearer ${token.getToken()!!}",
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

    suspend fun getMe():UserData? {
        if (token.hasToken()) {
            val response = grotesqueGeckoAPI.getMe(auth = "Bearer ${token.getToken()!!}").await()
            return response.body()
        }
        return null
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
            body = requestBody,
            id = id
        ).await()
    }

    suspend fun editComment(
            caffId: String,
            commentId: String,
            content: String
    ): retrofit2.Response<CaffComment> {
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .build()
        return grotesqueGeckoAPI.editComment(
                auth = "Bearer ${token.getToken()!!}",
                caffId = caffId,
                commentId = commentId,
                body = requestBody
        ).await()
    }

    suspend fun deleteComment(
            caffId: String,
            commentId: String
    ): retrofit2.Response<Void> {
        return grotesqueGeckoAPI.deleteComment(
                auth = "Bearer ${token.getToken()!!}",
                caffId = caffId,
                commentId = commentId
        ).await()
    }

    suspend fun createCaff(
            filePath: String,
            title: String,
            tags: String
    ): retrofit2.Response<Caff> {
        val file = File(filePath)
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("tags", tags)
                .addFormDataPart("title", title)
                .build()
        return grotesqueGeckoAPI.createCaff(
                auth = "Bearer ${token.getToken()!!}",
                body = requestBody
        ).await()
    }
}