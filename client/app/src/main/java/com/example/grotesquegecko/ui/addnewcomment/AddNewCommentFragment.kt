package com.example.grotesquegecko.ui.addnewcomment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_add_new_caff.*
import kotlinx.android.synthetic.main.fragment_add_new_comment.*

class AddNewCommentFragment : RainbowCakeFragment<AddNewCommentViewState, AddNewCommentViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_add_new_comment

    private lateinit var caffId: String
    private lateinit var caffTitle: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()

        addNewCommentBackButton.setOnClickListener {
            val bundle = bundleOf("caffId" to caffId, "caffTitle" to caffTitle)
            findNavController().navigate(
                    R.id.action_nav_add_new_comment_to_nav_caff_details, bundle
            )
        }

        addNewCommentSendButton.setOnClickListener {
            val comment: String = commentEditText.text.toString().trim()
            if (comment.isEmpty()) {
                commentEditText.error =
                        getString(R.string.comment_fragment_comment_required)
                commentEditText.requestFocus()
                return@setOnClickListener
            }

            viewModel.createComment(comment, caffId)
        }
    }

    private fun initArguments() {
        caffId = arguments?.getString("caffId").toString()
        caffTitle = arguments?.getString("caffTitle").toString()
    }

    override fun render(viewState: AddNewCommentViewState) {
        when (viewState) {
            is Comment -> comment()
            is CommentFailed -> commentFailed()
            is Loading -> loading()
            is AddNewCommentReady -> commentReady()
        }
    }

    private fun commentReady() {
        addNewCommentProgressBar.visibility = View.GONE
        textViewCommentFailed.visibility = View.GONE
        val bundle = bundleOf("caffId" to caffId, "caffTitle" to caffTitle)
        findNavController().navigate(
                R.id.action_nav_add_new_comment_to_nav_caff_details, bundle
        )
    }

    private fun loading() {
        addNewCommentProgressBar.visibility = View.VISIBLE
    }

    private fun commentFailed() {
        addNewCommentProgressBar.visibility = View.GONE
        textViewCommentFailed.visibility = View.VISIBLE
    }

    private fun comment() {
        addNewCommentProgressBar.visibility = View.GONE
        textViewCommentFailed.visibility = View.GONE
    }

}
