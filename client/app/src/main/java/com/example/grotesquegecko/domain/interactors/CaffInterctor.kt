package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.CaffPreview
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
}