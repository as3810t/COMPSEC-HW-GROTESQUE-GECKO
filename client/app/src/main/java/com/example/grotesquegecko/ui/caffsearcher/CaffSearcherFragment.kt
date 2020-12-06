package com.example.grotesquegecko.ui.caffsearcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.LoginActivity
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.caffdetails.CaffDetailsViewModel
import kotlinx.android.synthetic.main.fragment_caff_searcher.*
import timber.log.Timber

class CaffSearcherFragment :
    RainbowCakeFragment<CaffSearcherViewState, CaffSearcherViewModel>(),
    CaffSearcherAdapter.Listener {
    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_searcher

    private lateinit var adapter: CaffSearcherAdapter
    private lateinit var userId: String
    val SEARCH_BY_USER = 0
    val SEARCH_BY_TITLE = 1
    val SEARCH_BY_TAGS = 2

    var SEARCH_TYPE = SEARCH_BY_TITLE

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

        caffSelectorSearchField.setHint("Keresés cím szerint")

        caffSelectorIcon.setOnClickListener {
            when (SEARCH_TYPE) {
                SEARCH_BY_TITLE -> {
                    caffSelectorIcon.setImageResource(R.drawable.icon_tags)
                    SEARCH_TYPE = SEARCH_BY_TAGS
                    caffSelectorSearchField.isEnabled = true
                    caffSelectorSearchField.setText("")
                    caffSelectorSearchField.setHint("Keresés tagek szerint")
                }
                SEARCH_BY_TAGS -> {
                    caffSelectorIcon.setImageResource(R.drawable.icon_person)
                    SEARCH_TYPE = SEARCH_BY_USER
                    caffSelectorSearchField.isEnabled = false
                    caffSelectorSearchField.setText(getString(R.string.caff_searcher_my_caffs))
                }
                SEARCH_BY_USER -> {
                    caffSelectorIcon.setImageResource(R.drawable.icon_title)
                    SEARCH_TYPE = SEARCH_BY_TITLE
                    caffSelectorSearchField.isEnabled = true
                    caffSelectorSearchField.setText("")
                    caffSelectorSearchField.setHint("Keresés cím szerint")
                }
            }
        }

        caffSelectorSearchButton.setOnClickListener {
            when (SEARCH_TYPE) {
                SEARCH_BY_TITLE -> viewModel.getCaffListByParameter(
                    tag = "",
                    title = caffSelectorSearchField.text.toString(),
                    userId = ""
                )
                SEARCH_BY_TAGS -> viewModel.getCaffListByParameter(
                    tag = caffSelectorSearchField.text.toString(),
                    title = "",
                    userId = ""
                )
                SEARCH_BY_USER -> viewModel.getCaffListByParameter(
                    tag = "",
                    title = "",
                    userId = userId
                )
            }
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

        viewModel.getUserId()
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is CaffSearcherViewModel.Logout -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is CaffDetailsViewModel.WrongToken -> showWrongToken()
            is CaffDetailsViewModel.UserId -> setupUserId(event)
        }
    }

    private fun showWrongToken() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun setupUserId(event: CaffDetailsViewModel.UserId) {
        userId = event.userId
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

    override fun onItemSelected(id: String, title: String) {
        Timber.i("caff details")
        val bundle = bundleOf("caffId" to id, "caffTitle" to title)
        findNavController().navigate(
            R.id.action_nav_caffs_to_nav_caff_details, bundle
        )
    }
}
