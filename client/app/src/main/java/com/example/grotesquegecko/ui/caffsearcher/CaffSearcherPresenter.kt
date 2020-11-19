package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class CaffSearcherPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
