package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.CaffPreview
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import com.example.grotesquegecko.domain.interactors.UserInteractor
import javax.inject.Inject

class CaffSearcherPresenter @Inject constructor(
    private val caffInterctor: CaffInterctor,
    private val userInteractor: UserInteractor
) {
    suspend fun getCaffList(): MutableList<CaffPreview> = withIOContext {
        caffInterctor.getCaffList()
    }

    suspend fun logout(): Boolean {
        return userInteractor.logout()
    }
}
