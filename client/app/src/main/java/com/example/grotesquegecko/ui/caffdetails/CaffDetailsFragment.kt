package com.example.grotesquegecko.ui.caffdetails

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.common.glideLoader
import kotlinx.android.synthetic.main.fragment_caff_details.*

class CaffDetailsFragment : RainbowCakeFragment<CaffDetailsViewState, CaffDetailsViewModel>(),
    CaffDetailsAdapter.Listener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_details

    private lateinit var caffId: String
    private lateinit var caffTitle: String

    private lateinit var adapter: CaffDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()

        caffDetailsEmptyListText.text = "$caffTitle comments"

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
        setupList()
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
        adapter = CaffDetailsAdapter(requireContext())
        adapter.listener = this
        caffDetailsCommentList.adapter = adapter
        caffDetailsCommentList.emptyView = caffDetailsEmptyListText
    }

    private fun initArguments() {
        caffId = arguments?.getString("caffId").toString()
        caffTitle = arguments?.getString("caffTitle").toString()
    }

    override fun onStart() {
        super.onStart()

        viewModel.load(caffId)
    }

    override fun render(viewState: CaffDetailsViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is CaffDetailsReady -> showCommentList(viewState)
        }
    }

    private fun showCommentList(viewState: CaffDetailsReady) {
        caffDetailsProgressBar.visibility = View.GONE
        adapter.submitList(viewState.data)
    }

    private fun showLoading() {
        caffDetailsProgressBar.visibility = View.VISIBLE
    }

    override fun onItemSelected(id: String) {
        TODO("Not yet implemented")
    }

}
