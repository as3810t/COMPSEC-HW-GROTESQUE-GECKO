package com.example.grotesquegecko.ui.addnewcaff

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class AddNewCaffViewModel @Inject constructor(
    private val addNewCaffPresenter: AddNewCaffPresenter
) : RainbowCakeViewModel<AddNewCaffViewState>(Loading) {

    fun load() = execute {
        viewState = AddNewCaffReady(addNewCaffPresenter.getData())
    }

}
