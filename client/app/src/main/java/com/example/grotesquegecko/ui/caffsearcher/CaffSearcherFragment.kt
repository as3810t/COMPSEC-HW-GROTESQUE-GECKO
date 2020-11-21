package com.example.grotesquegecko.ui.caffsearcher

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_caff_searcher.*
import timber.log.Timber

class CaffSearcherFragment :
    RainbowCakeFragment<CaffSearcherViewState, CaffSearcherViewModel>(),
    CaffSearcherAdapter.Listener {
    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_searcher

    private lateinit var adapter: CaffSearcherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
    }

    private fun setupList() {
        adapter = CaffSearcherAdapter(requireContext())
        adapter.listener = this
        caffSearcherCaffList.adapter = adapter
        caffSearcherCaffList.emptyView = caffSearcherEmptyListText
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: CaffSearcherViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is CaffSearchReady -> showCaffList(viewState)
        }
    }

    private fun showLoading() {
        caffSearcherProgressBar.visibility = View.VISIBLE
    }

    private fun showCaffList(viewState: CaffSearchReady) {
        caffSearcherProgressBar.visibility = View.GONE
        adapter.submitList(viewState.data)
    }

    override fun onItemSelected(id: String) {
        //TODO show caff details
        Timber.i("caff details")
    }
}
