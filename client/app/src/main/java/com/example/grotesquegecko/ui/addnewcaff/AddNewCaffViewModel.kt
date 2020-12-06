package com.example.grotesquegecko.ui.addnewcaff

import android.net.Uri
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import java.io.File
import javax.inject.Inject

class AddNewCaffViewModel @Inject constructor(
    private val addNewCaffPresenter: AddNewCaffPresenter
) : RainbowCakeViewModel<AddNewCaffViewState>(Caff) {

    fun createCaff(
        filePath: Uri?,
        title: String,
        tags: String,
        file: File
    ) = execute {
        viewState = Loading
        val successful = addNewCaffPresenter.createCaff(filePath, title, tags, file)
        viewState = if (successful) {
            AddNewCaffReady(successful)
        } else {
            CaffFailed
        }
    }

}
