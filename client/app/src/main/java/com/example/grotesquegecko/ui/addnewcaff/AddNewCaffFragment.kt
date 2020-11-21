package com.example.grotesquegecko.ui.addnewcaff

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_add_new_caff.*

class AddNewCaffFragment : RainbowCakeFragment<AddNewCaffViewState, AddNewCaffViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_add_new_caff

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        addNewCaffBackButton.setOnClickListener {
            findNavController().navigate(
                AddNewCaffFragmentDirections.actionNavAddNewCaffToNavCaffs()
            )
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: AddNewCaffViewState) {
        // TODO Render state
    }

}
