package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class CaffSearcherViewModel @Inject constructor(
    private val caffSearcherPresenter: CaffSearcherPresenter
) : RainbowCakeViewModel<CaffSearcherViewState>(Loading) {

    object Logout : OneShotEvent

    fun load() = execute {
        viewState = CaffSearchReady(caffSearcherPresenter.getCaffList())
    }

    fun logout() = execute {
        if (caffSearcherPresenter.logout()) {
            postEvent(Logout)
        }
    }
}
