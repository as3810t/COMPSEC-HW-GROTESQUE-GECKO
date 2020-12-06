package com.example.grotesquegecko.domain.interactors

import android.net.Uri
import com.example.grotesquegecko.data.network.NetworkDataSource
import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.CaffPreview
import java.io.File
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CaffInterctor @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend fun getCaffList(): MutableList<CaffPreview> {
        return networkDataSource.getCaffList()
    }

    suspend fun getCommentList(id: String): MutableList<CaffComment> {
        return networkDataSource.getCommentList(id)
    }

    suspend fun createComment(content: String, id: String): Boolean {
        val response = networkDataSource.createComment(content, id)
        return response.code() == 200 && response.body() != null
    }

    suspend fun downloadCaff(id: String): Response<ResponseBody>? {
        return networkDataSource.downloadCaff(id)
    }

    suspend fun editComment(caffId: String, commentId: String, content: String): Boolean {
        val response = networkDataSource.editComment(caffId, commentId, content)
        return response.code() == 200 && response.body() != null
    }

    suspend fun deleteComment(caffId: String, commentId: String): Boolean {
        val response = networkDataSource.deleteComment(caffId, commentId)
        return response.code() == 200 && response.body() != null
    }

    suspend fun createCaff(
        filePath: Uri?,
        title: String,
        tags: String,
        file: File
    ): Boolean {
        var tagList = tags.split(",")
        var tagOneString = ""
        for (i in 0 until tagList.size - 1) {
            tagOneString = tagOneString + tagList[i] + "|"
        }
        tagOneString += tagList[tagList.size - 1]
        val response = networkDataSource.createCaff(filePath, title, tagOneString, file)
        return response.code() == 200 && response.body() != null
    }
}