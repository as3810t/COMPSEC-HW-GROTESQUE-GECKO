package com.example.grotesquegecko.ui.allusers

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_all_users.*

class AllUsersFragment : RainbowCakeFragment<AllUsersViewState, AllUsersViewModel>(),
    AllUsersAdapter.Listener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_all_users

    private lateinit var adapter: AllUsersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
    }

    private fun setupList() {
        adapter = AllUsersAdapter(requireContext())
        adapter.listener = this
        allUsersList.adapter = adapter
        allUsersList.emptyView = allUsersEmptyListText
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: AllUsersViewState) {
        when (viewState) {
            is Loading -> allUsersProgressBar.visibility = View.VISIBLE
            is AllUsersReady -> showAllUsers(viewState)
        }
    }

    private fun showAllUsers(viewState: AllUsersReady) {
        allUsersProgressBar.visibility = View.GONE
        adapter.submitList(viewState.allUsers)
    }

    override fun onEditItemSelected(id: String) {
        MaterialDialog(requireContext()).show {
            noAutoDismiss()
            title(text = getString(R.string.user_data_change_username)).view.titleLayout.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            input()
            positiveButton(text = getString(R.string.login_fragment_forgotten_password_ok)) {
                val username = it.getInputField().text.toString()
                if (username.isNotEmpty()) {
                    viewModel.editUserData(
                        username = username,
                        email = "",
                        password = "",
                        id = id
                    )
                    it.dismiss()
                } else {
                    it.getInputField().error = "Required"
                }
            }
            negativeButton(text = getString(R.string.login_fragment_forgotten_password_cancel)) {
                it.dismiss()
            }
                .view.setBackgroundColor(ContextCompat.getColor(context, R.color.darkGray))

        }
    }

    override fun onDeleteItemSelected(id: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Biztosan törölni akarod a felhasználót?")
            .setMessage("Ezzel véglegesen törlöd az adatbázisból a felhasználót.")
            .setPositiveButton(R.string.logout_dialog_ok) { _, _ ->
                viewModel.deleteUser(id)
            }
            .setNegativeButton(R.string.logout_dialog_cancel, null)
            .show()
    }

    override fun onChangePasswordItemSelected(id: String) {
        MaterialDialog(requireContext()).show {
            noAutoDismiss()
            title(text = getString(R.string.user_data_password_successfully_changed)).view.titleLayout.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            positiveButton(text = getString(R.string.login_fragment_forgotten_password_ok)) {
                dismiss()
            }
                .view.setBackgroundColor(ContextCompat.getColor(context, R.color.darkGray))

        }
    }
}
