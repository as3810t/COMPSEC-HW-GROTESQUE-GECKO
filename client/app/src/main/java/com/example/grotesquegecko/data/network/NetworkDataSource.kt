package com.example.grotesquegecko.data.network

import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.CaffPreview
import com.example.grotesquegecko.data.network.models.LoginData
import com.example.grotesquegecko.data.network.models.UserData
import android.content.Context
import android.net.Uri
import com.example.grotesquegecko.data.network.models.*
import com.example.grotesquegecko.data.network.token.Token
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor(
    private val grotesqueGeckoAPI: GrotesqueGeckoAPI,
    private val token: Token,
    private val context: Context
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

    suspend fun getMe(): UserData? {
        if (token.hasToken()) {
            val response = grotesqueGeckoAPI.getMe(auth = "Bearer ${token.getToken()!!}").await()
            return response.body()
        }
        return null
    }

    suspend fun editUserData(
        email: String,
        password: String,
        username: String
    ): Boolean {
        val me = getMe()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        if (token.hasToken() and (me != null)) {
            val response = grotesqueGeckoAPI.editUserData(
                auth = "Bearer ${token.getToken()!!}",
                id = me!!.id,
                body = requestBody
            ).await()
            return response.isSuccessful
        } else return false
    }

    suspend fun editUserData(
        email: String,
        password: String,
        username: String,
        id: String
    ): Boolean {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("username", username)
            .build()
        if (token.hasToken()) {
            val response = grotesqueGeckoAPI.editUserData(
                auth = "Bearer ${token.getToken()!!}",
                id = id,
                body = requestBody
            ).await()
            return response.isSuccessful
        } else return false
    }

    suspend fun getAllUsers(): MutableList<UserData> {
        if (!token.hasToken()) {
            return mutableListOf()
        }
        val response = grotesqueGeckoAPI.getAllUsers(
            auth = "Bearer ${token.getToken()!!}",
            offset = null,
            pageSize = null
        ).await()

        return if (response.isSuccessful and (response.body() != null)) {
            response.body()!!.users
        } else {
            mutableListOf()
        }
    }

    suspend fun deleteUser(userId: String) {
        if (token.hasToken()) {
            grotesqueGeckoAPI.deleteUser(
                auth = "Bearer ${token.getToken()!!}",
                id = userId
            ).await()
        }
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
        fileUri: Uri?,
        title: String,
        tags: String,
        selectedFile: File
    ): retrofit2.Response<Caff> {
        val requestBody: MultipartBody.Builder = MultipartBody.Builder()
        requestBody.apply {
            setType(MultipartBody.FORM)
            addFormDataPart(
                "file",
                "${selectedFile.name}.caff",
                selectedFile.asRequestBody(
                    fileUri?.let { context.contentResolver.getType(it) }!!.toMediaTypeOrNull()
                )
            )
            addFormDataPart("tags", tags)
            addFormDataPart("title", title)
        }
        return grotesqueGeckoAPI.createCaff(
            auth = "Bearer ${token.getToken()!!}",
            body = requestBody.build()
        ).await()
    }
}