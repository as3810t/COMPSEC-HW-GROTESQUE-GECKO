package com.example.grotesquegecko.ui.caffdetails

import com.example.grotesquegecko.data.network.models.Caff
import com.example.grotesquegecko.data.network.models.CaffComment

sealed class CaffDetailsViewState

object Loading : CaffDetailsViewState()

data class CaffDetailsUserReady(
    val comments: MutableList<CaffComment>,
    val caffData: Caff
) : CaffDetailsViewState()
