package com.example.grotesquegecko.ui.addnewcaff

sealed class AddNewCaffViewState

object Caff : AddNewCaffViewState()
object Loading : AddNewCaffViewState()
object CaffFailed : AddNewCaffViewState()

data class AddNewCaffReady(val caffPosted: Boolean) : AddNewCaffViewState()
