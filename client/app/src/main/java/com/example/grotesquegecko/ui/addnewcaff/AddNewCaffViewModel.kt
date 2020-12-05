package com.example.grotesquegecko.ui.addnewcaff

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.example.grotesquegecko.ui.addnewcomment.AddNewCommentReady
import com.example.grotesquegecko.ui.addnewcomment.CommentFailed
import javax.inject.Inject

class AddNewCaffViewModel @Inject constructor(
    private val addNewCaffPresenter: AddNewCaffPresenter
) : RainbowCakeViewModel<AddNewCaffViewState>(Caff) {

    fun createCaff(filePath: String, title: String, tags: String) = execute {
        viewState = Loading
        val successful = addNewCaffPresenter.createCaff(filePath, title, tags)
        viewState = if (successful) {
            AddNewCaffReady(successful)
        } else {
            CaffFailed
        }
    }

}
