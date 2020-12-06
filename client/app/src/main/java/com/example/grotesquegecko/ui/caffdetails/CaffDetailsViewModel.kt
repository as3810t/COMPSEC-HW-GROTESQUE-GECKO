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
    ) : OneShotEvent

    object WrongToken : OneShotEvent
    object ErrorDuringLoadCaffDatas : OneShotEvent
    object ErrorDuringEditCaffDatas : OneShotEvent
    object CaffWasDeleted : OneShotEvent
    object CaffDeleteFailed : OneShotEvent

    data class Download(
        var response: Response<ResponseBody>?
    ) : OneShotEvent

    object DownloadWasNotSuccessful : OneShotEvent

    fun load(id: String) = execute {
        val caffData = caffDetailsPresenter.getCaffById(id)
        if (caffData == null) {
            postEvent(ErrorDuringLoadCaffDatas)
        } else {
            viewState = CaffDetailsUserReady(caffDetailsPresenter.getCommentList(id), caffData)
        }
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

    fun editCaffDatas(title: String, tags: String, caffId: String) = execute {
        val caffDatas = caffDetailsPresenter.editCaffDatas(
            caffId = caffId,
            title = title,
            tags = tags
        )
        if (caffDatas == null) {
            postEvent(ErrorDuringEditCaffDatas)
        } else {
            viewState =
                CaffDetailsUserReady((viewState as CaffDetailsUserReady).comments, caffDatas)
        }
    }

    fun deleteCaff(caffId: String) = execute {
        if (caffDetailsPresenter.deleteCaff(caffId)) {
            postEvent(CaffWasDeleted)
        } else postEvent(CaffDeleteFailed)
    }
}
