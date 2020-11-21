package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class CaffSearcherViewModel @Inject constructor(
    private val caffSearcherPresenter: CaffSearcherPresenter
) : RainbowCakeViewModel<CaffSearcherViewState>(Loading) {

    fun load() = execute {
        viewState = CaffSearchReady(caffSearcherPresenter.getCaffList())
    }

}
