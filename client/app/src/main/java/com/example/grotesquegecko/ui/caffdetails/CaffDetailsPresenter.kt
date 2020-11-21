package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class CaffDetailsPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
