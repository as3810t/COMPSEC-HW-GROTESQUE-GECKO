package com.example.grotesquegecko.ui.caffdetails

sealed class CaffDetailsViewState

object Loading : CaffDetailsViewState()

data class CaffDetailsReady(val data: String = "") : CaffDetailsViewState()
