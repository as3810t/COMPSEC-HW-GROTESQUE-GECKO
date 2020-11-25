package com.example.grotesquegecko.ui.caffdetails

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_caff_details.*

class CaffDetailsFragment : RainbowCakeFragment<CaffDetailsViewState, CaffDetailsViewModel>(),
    CaffDetailsAdapter.Listener{

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_details

    private lateinit var caffId: String
    private lateinit var caffTitle: String

    private lateinit var adapter : CaffDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()

        caffDetailsEmptyListText.text = caffId

        caffDetailsBackButton.setOnClickListener {
            findNavController().navigate(
                CaffDetailsFragmentDirections.actionNavCaffDetailsToNavCaffs()
            )
        }

        setupList()
        Handler(Looper.getMainLooper()).post {
            Picasso
                    .with(context)
                    .load("https://gecko.stripedpossum.dev/caff/${caffId}/preview")
                    .placeholder(R.drawable.icon_gecko)
                    .into(caffDetailsCaffPreview)
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
