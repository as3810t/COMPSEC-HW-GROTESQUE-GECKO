package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import javax.inject.Inject

class CaffDetailsPresenter @Inject constructor(
        private val caffInterctor: CaffInterctor
) {

    suspend fun getCommentList(id: String): MutableList<CaffComment> = withIOContext {
        caffInterctor.getCommentList(id)
    }

}
