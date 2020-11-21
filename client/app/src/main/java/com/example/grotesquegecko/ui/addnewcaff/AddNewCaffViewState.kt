package com.example.grotesquegecko.ui.addnewcaff

sealed class AddNewCaffViewState

object Loading : AddNewCaffViewState()

data class AddNewCaffReady(val data: String = "") : AddNewCaffViewState()
