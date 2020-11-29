package com.example.grotesquegecko.ui.addnewcomment

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class AddNewCommentViewModel @Inject constructor(
    private val addNewCommentPresenter: AddNewCommentPresenter
) : RainbowCakeViewModel<AddNewCommentViewState>(Comment) {

    fun createComment(content: String, id: String) = execute {
        viewState = Loading
        val successful = addNewCommentPresenter.createComment(content, id)
        viewState = if (successful) {
            AddNewCommentReady(successful)
        } else {
            CommentFailed
        }
    }

}
