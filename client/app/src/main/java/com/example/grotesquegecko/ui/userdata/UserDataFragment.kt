package com.example.grotesquegecko.ui.userdata

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
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
        }
    }

    override fun render(viewState: UserDataViewState) {
        // TODO Render state
    }

}
