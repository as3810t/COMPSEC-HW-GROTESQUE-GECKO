package com.example.grotesquegecko.domain.interactors

import com.example.grotesquegecko.data.network.NetworkDataSource
import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.CaffPreview
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
}