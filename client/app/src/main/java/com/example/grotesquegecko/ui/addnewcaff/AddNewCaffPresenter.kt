package com.example.grotesquegecko.ui.addnewcaff

import android.net.Uri
import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.domain.interactors.CaffInterctor
import java.io.File
import javax.inject.Inject

class AddNewCaffPresenter @Inject constructor(
        private val caffInterctor: CaffInterctor
) {

    suspend fun createCaff(
        filePath: Uri?,
        title: String,
        tags: String,
        file: File
    ): Boolean = withIOContext {
        return@withIOContext caffInterctor.createCaff(filePath, title, tags, file)
    }

}
