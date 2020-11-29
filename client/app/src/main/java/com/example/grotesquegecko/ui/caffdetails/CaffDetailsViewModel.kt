package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.example.grotesquegecko.data.network.models.UserData
import javax.inject.Inject

class CaffDetailsViewModel @Inject constructor(
    private val caffDetailsPresenter: CaffDetailsPresenter
) : RainbowCakeViewModel<CaffDetailsViewState>(Loading) {
    data class UserId(
            var userId: String
    ): OneShotEvent

    object WrongToken: OneShotEvent

    fun load(id: String) = execute {
        viewState = CaffDetailsReady(caffDetailsPresenter.getCommentList(id))
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
}
