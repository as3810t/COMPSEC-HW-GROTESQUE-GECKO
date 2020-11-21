package com.example.grotesquegecko.ui.caffdetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_caff_details.*

class CaffDetailsFragment : RainbowCakeFragment<CaffDetailsViewState, CaffDetailsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        caffDetailsBackButton.setOnClickListener {
            findNavController().navigate(
                CaffDetailsFragmentDirections.actionNavCaffDetailsToNavCaffs()
            )
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: CaffDetailsViewState) {
        // TODO Render state
    }

}
