package com.example.grotesquegecko.ui.caffsearcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.LoginActivity
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

        setupButtons()
        setupList()
    }

    private fun setupButtons() {
        caffSearcherAddNewCaffsButton.setOnClickListener {
            findNavController().navigate(
                CaffSearcherFragmentDirections.actionNavCaffsToNavAddNewCaff()
            )
        }

        caffSearcherLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_text)
                .setPositiveButton(R.string.logout_dialog_ok) { _, _ ->
                    viewModel.logout()
                }
                .setNegativeButton(R.string.logout_dialog_cancel, null)
                .show()
        }
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

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is CaffSearcherViewModel.Logout -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
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
        Timber.i("caff details")
        findNavController().navigate(
            CaffSearcherFragmentDirections.actionNavCaffsToNavCaffDetails()
        )
    }
}
