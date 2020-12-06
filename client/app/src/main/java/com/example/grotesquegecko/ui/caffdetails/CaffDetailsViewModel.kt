package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CaffDetailsViewModel @Inject constructor(
    private val caffDetailsPresenter: CaffDetailsPresenter
) : RainbowCakeViewModel<CaffDetailsViewState>(Loading) {
    data class UserId(
            var userId: String
    ): OneShotEvent

    object WrongToken: OneShotEvent

    data class Download(
        var response: Response<ResponseBody>?
    ) : OneShotEvent

    object DownloadWasNotSuccessful : OneShotEvent

    fun load(id: String) = execute {
        viewState = CaffDetailsReady(caffDetailsPresenter.getCommentList(id))
    }

    fun downloadCaff(id: String) = execute {
        val inputStream = caffDetailsPresenter.downloadCaff(id)
        if (inputStream == null) {
            postEvent(DownloadWasNotSuccessful)
        } else {
            postEvent(Download(inputStream))
        }
    }
    fun getUserId() = execute {
        val user = caffDetailsPresenter.getMe()
        if (user == null) {
            postEvent(WrongToken)
        } else {
            postEvent(UserId(user.id))
        }
    }

    fun editComment(caffId: String, commentId: String, content: String) = execute {
        caffDetailsPresenter.editComment(caffId, commentId, content)
        load(caffId)
    }

    fun deleteComment(caffId: String, commentId: String) = execute {
        caffDetailsPresenter.deleteComment(caffId, commentId)
        load(caffId)
    }

    fun myAccountIsUser(): Boolean {
        return caffDetailsPresenter.myAccountIsUser()
    }
}
