package com.example.grotesquegecko.ui.addnewcomment

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import javax.inject.Inject

class AddNewCommentPresenter @Inject constructor(
        private val caffInterctor: CaffInterctor
) {

    suspend fun createComment(content: String, id: String): Boolean = withIOContext {
        return@withIOContext caffInterctor.createComment(content, id)
    }

}
