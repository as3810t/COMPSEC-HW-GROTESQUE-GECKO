package com.example.grotesquegecko.ui.userdata

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class UserDataPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
