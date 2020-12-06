package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.example.grotesquegecko.ui.caffdetails.CaffDetailsViewModel
import javax.inject.Inject

class CaffSearcherViewModel @Inject constructor(
    private val caffSearcherPresenter: CaffSearcherPresenter
) : RainbowCakeViewModel<CaffSearcherViewState>(Loading) {

    object Logout : OneShotEvent
    data class UserId(
        var userId: String
    ) : OneShotEvent

    object WrongToken : OneShotEvent

    fun load() = execute {
        viewState = CaffSearchReady(
            caffSearcherPresenter.getCaffList(
                tag = "",
                title = "",
                userId = ""
            )
        )
    }

    fun logout() = execute {
        if (caffSearcherPresenter.logout()) {
            postEvent(Logout)
        }
    }

    fun getCaffListByParameter(tag: String, title: String, userId: String) = execute {
        viewState = CaffSearchReady(
            caffSearcherPresenter.getCaffList(
                tag = tag,
                title = title,
                userId = userId
            )
        )
    }

    fun getUserId() = execute {
        val user = caffSearcherPresenter.getMe()
        if (user == null) {
            postEvent(CaffDetailsViewModel.WrongToken)
        } else {
            postEvent(CaffDetailsViewModel.UserId(user.id))
        }
    }
}
