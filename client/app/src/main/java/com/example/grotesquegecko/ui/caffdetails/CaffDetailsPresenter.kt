package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.data.network.models.CaffComment
import com.example.grotesquegecko.data.network.models.UserData
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import com.example.grotesquegecko.domain.interactors.UserInteractor
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CaffDetailsPresenter @Inject constructor(
        private val caffInterctor: CaffInterctor,
        private val userInteractor: UserInteractor
) {

    suspend fun getCommentList(id: String): MutableList<CaffComment> = withIOContext {
        caffInterctor.getCommentList(id)
    }

    suspend fun downloadCaff(id: String): Response<ResponseBody>? = withIOContext {
        return@withIOContext caffInterctor.downloadCaff(id)
    }
    suspend fun getMe(): UserData? = withIOContext {
        userInteractor.getUserData()
    }

    suspend fun editComment(caffId: String, commentId: String, content: String) = withIOContext {
        caffInterctor.editComment(caffId, commentId, content)
    }

    suspend fun deleteComment(caffId: String, commentId: String) = withIOContext {
        caffInterctor.deleteComment(caffId, commentId)
    }

}
