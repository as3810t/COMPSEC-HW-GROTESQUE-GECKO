package com.example.grotesquegecko.ui.caffsearcher

sealed class CaffSearcherViewState

object Loading : CaffSearcherViewState()

data class CaffSearchReady(val data: String = "") : CaffSearcherViewState()
