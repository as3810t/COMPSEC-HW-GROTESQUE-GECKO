package com.example.grotesquegecko.ui.caffsearcher

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
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

        val caffList = mutableListOf<CaffPreview>(
            CaffPreview(
                "1",
                "Grotesque",
                "https://scx2.b-cdn.net/gfx/news/2017/2-5-universityof.jpg"
            ),
            CaffPreview(
                "2",
                "Gecko",
                "https://i.insider.com/53bdb318ecad04c707b61b17?width=700&format=jpeg&auto=webp"
            )
        )

        adapter.submitList(caffList)
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: CaffSearcherViewState) {
        // TODO Render state
    }

    override fun onItemSelected(id: String) {
        //TODO show caff details
        Timber.i("caff details")
    }
}
