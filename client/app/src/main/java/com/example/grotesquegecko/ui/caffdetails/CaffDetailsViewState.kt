package com.example.grotesquegecko.ui.caffdetails

import com.example.grotesquegecko.data.network.models.CaffComment

sealed class CaffDetailsViewState

object Loading : CaffDetailsViewState()

data class CaffDetailsReady(val data: MutableList<CaffComment>) : CaffDetailsViewState()
