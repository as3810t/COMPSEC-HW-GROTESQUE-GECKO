package com.example.grotesquegecko.ui.addnewcomment

sealed class AddNewCommentViewState

object Comment : AddNewCommentViewState()
object Loading : AddNewCommentViewState()
object CommentFailed : AddNewCommentViewState()

data class AddNewCommentReady(val commentPosted: Boolean) : AddNewCommentViewState()
