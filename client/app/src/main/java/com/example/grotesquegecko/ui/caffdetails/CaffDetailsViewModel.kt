package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class CaffDetailsViewModel @Inject constructor(
    private val caffDetailsPresenter: CaffDetailsPresenter
) : RainbowCakeViewModel<CaffDetailsViewState>(Loading) {

    fun load() = execute {
        viewState = CaffDetailsReady(caffDetailsPresenter.getData())
    }

}
