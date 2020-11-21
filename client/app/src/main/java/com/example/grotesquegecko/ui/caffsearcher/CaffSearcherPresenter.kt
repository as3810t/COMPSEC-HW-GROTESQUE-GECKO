package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
import javax.inject.Inject

class CaffSearcherPresenter @Inject constructor(
    private val caffInterctor: CaffInterctor
) {

    suspend fun getCaffList(): MutableList<CaffPreview> = withIOContext {
        caffInterctor.getCaffList()
    }

}
