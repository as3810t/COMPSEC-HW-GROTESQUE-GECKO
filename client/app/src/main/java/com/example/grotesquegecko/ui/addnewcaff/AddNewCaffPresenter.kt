package com.example.grotesquegecko.ui.addnewcaff

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class AddNewCaffPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
