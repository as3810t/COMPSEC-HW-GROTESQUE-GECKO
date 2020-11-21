package com.example.grotesquegecko.ui.caffsearcher

import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview

sealed class CaffSearcherViewState

object Loading : CaffSearcherViewState()

data class CaffSearchReady(val data: MutableList<CaffPreview>) : CaffSearcherViewState()
