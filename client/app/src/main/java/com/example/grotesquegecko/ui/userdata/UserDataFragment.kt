package com.example.grotesquegecko.ui.userdata

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.grotesquegecko.LoginActivity
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_user_data.*

class UserDataFragment : RainbowCakeFragment<UserDataViewState, UserDataViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_user_data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        userDataLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_text)
                .setPositiveButton(R.string.logout_dialog_ok) { _, _ ->
                    viewModel.logout()
                }
                .setNegativeButton(R.string.logout_dialog_cancel, null)
                .show()
        }

        userDataEditUsernameButton.setOnClickListener {
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
                            password = ""
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

        userDataEditEmailButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                noAutoDismiss()
                title(text = getString(R.string.user_data_change_email)).view.titleLayout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                )
                input()
                positiveButton(text = getString(R.string.login_fragment_forgotten_password_ok)) {
                    val email = it.getInputField().text.toString()
                    if (email.isNotEmpty() and Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        viewModel.editUserData(
                            username = "",
                            email = email,
                            password = ""
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

        userDataEditPasswordButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                noAutoDismiss()
                title(text = getString(R.string.user_data_change_password)).view.titleLayout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                )
                input()
                positiveButton(text = getString(R.string.login_fragment_forgotten_password_ok)) {
                    val password = it.getInputField().text.toString()
                    if (password.isNotEmpty()) {
                        viewModel.editUserData(
                            username = "",
                            email = "",
                            password = password
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
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is UserDataViewModel.Logout -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is UserDataViewModel.EditWasSuccessful -> {
                userDataEditFeedback.text = getString(R.string.user_data_changes_saved)
            }
            is UserDataViewModel.EditWasNotSuccessful -> {
                userDataEditFeedback.text = getString(R.string.user_data_changes_are_not_saved)
            }
            is UserDataViewModel.UserInformation -> {
                userDataEmail.text = event.email
                userDataUsername.text = event.username
            }
        }
    }

    override fun render(viewState: UserDataViewState) {
        when (viewState) {
            is Loading -> {
                viewModel.myAccountIsUser()
            }
            is User -> {

                userDataAdmin.visibility = View.GONE
                userDataShowUsersButton.visibility = View.GONE
                divider3.visibility = View.GONE
            }
            is Admin -> {
                userDataAdmin.visibility = View.VISIBLE
                userDataShowUsersButton.visibility = View.VISIBLE
                divider3.visibility = View.VISIBLE
            }
        }
    }

}
