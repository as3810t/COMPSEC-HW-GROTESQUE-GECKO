package com.example.grotesquegecko.ui.caffdetails

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.LoginActivity
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.common.glideLoader
import kotlinx.android.synthetic.main.fragment_caff_details.*

class CaffDetailsFragment : RainbowCakeFragment<CaffDetailsViewState, CaffDetailsViewModel>(),
    CaffDetailsAdapter.Listener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_details

    private lateinit var caffId: String
    private lateinit var caffTitle: String
    private lateinit var userId: String

    private lateinit var adapter: CaffDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()

        caffDetailsEmptyListText.text = getString(R.string.caff_details_comments_empty_list_text)

        caffDetailsBackButton.setOnClickListener {
            findNavController().navigate(
                CaffDetailsFragmentDirections.actionNavCaffDetailsToNavCaffs()
            )
        }

        caffDetailsAddNewCommentButton.setOnClickListener {
            val bundle = bundleOf("caffId" to caffId, "caffTitle" to caffTitle)
            findNavController().navigate(
                R.id.action_nav_caff_details_to_nav_add_new_comment, bundle
            )
        }

        Handler(Looper.getMainLooper()).post {
            glideLoader(
                requireContext(),
                caffDetailsCaffPreview,
                "https://gecko.stripedpossum.dev/caff/${caffId}/preview"
            )
        }
        caffListTitle.text = caffTitle
    }

    private fun setupList() {
        adapter = CaffDetailsAdapter(requireContext(), userId)
        adapter.listener = this
        caffDetailsCommentList.adapter = adapter
        caffDetailsCommentList.emptyView = caffDetailsEmptyListText
    }

    override fun onStart() {
        super.onStart()

        viewModel.getUserId()
    }

    private fun initArguments() {
        caffId = arguments?.getString("caffId").toString()
        caffTitle = arguments?.getString("caffTitle").toString()
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is CaffDetailsViewModel.WrongToken -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is CaffDetailsViewModel.UserId -> {
                userId = event.userId
                viewModel.load(caffId)

            }
        }
    }

    override fun render(viewState: CaffDetailsViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is CaffDetailsReady -> showCommentList(viewState)
        }
    }

    private fun showCommentList(viewState: CaffDetailsReady) {
        setupList()
        caffDetailsProgressBar.visibility = View.GONE
        adapter.submitList(viewState.data)
    }

    private fun showLoading() {
        caffDetailsProgressBar.visibility = View.VISIBLE
    }

    override fun onCommentEdit(id: String, content: String) {
        viewModel.editComment(caffId, id, content)
    }

    override fun onCommentDelete(id: String) {
        viewModel.deleteComment(caffId, id)
    }

}
