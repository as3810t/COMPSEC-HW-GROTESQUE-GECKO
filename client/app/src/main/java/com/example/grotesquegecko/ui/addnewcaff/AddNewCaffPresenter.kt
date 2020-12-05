package com.example.grotesquegecko.ui.addnewcaff

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import javax.inject.Inject

class AddNewCaffPresenter @Inject constructor(
        private val caffInterctor: CaffInterctor
) {

    suspend fun createCaff(filePath: String, title: String, tags: String): Boolean = withIOContext {
        return@withIOContext caffInterctor.createCaff(filePath, title, tags)
    }

}
