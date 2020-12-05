package com.example.grotesquegecko.ui.caffdetails

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CaffDetailsViewModel @Inject constructor(
    private val caffDetailsPresenter: CaffDetailsPresenter
) : RainbowCakeViewModel<CaffDetailsViewState>(Loading) {

    data class Download(
        var response: Response<ResponseBody>?
    ) : OneShotEvent

    object DownloadWasNotSuccessful : OneShotEvent

    fun load(id: String) = execute {
        viewState = CaffDetailsReady(caffDetailsPresenter.getCommentList(id))
    }

    fun downloadCaff(id: String) = execute {
        val inputStream = caffDetailsPresenter.downloadCaff(id)
        if (inputStream == null) {
            postEvent(DownloadWasNotSuccessful)
        } else {
            postEvent(Download(inputStream))
        }
    }
}
