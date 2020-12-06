package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.CaffPreview
import com.example.grotesquegecko.data.network.models.UserData
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class CaffSearcherPresenter @Inject constructor(
    private val caffInterctor: CaffInterctor,
    private val userInteractor: UserInteractor
) {
    suspend fun getCaffList(tag: String, title: String, userId: String): MutableList<CaffPreview> =
        withIOContext {
            caffInterctor.getCaffList(
                tag = tag,
                title = title,
                userId = userId
            )
        }

    suspend fun logout(): Boolean {
        return userInteractor.logout()
    }

    suspend fun getMe(): UserData? = withIOContext {
        userInteractor.getUserData()
    }
}
