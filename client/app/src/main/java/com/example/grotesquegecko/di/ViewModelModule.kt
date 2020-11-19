package com.example.grotesquegecko.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import com.example.grotesquegecko.ui.blank.BlankViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BlankViewModel::class)
    abstract fun bindBlankViewModel(blankViewModel: BlankViewModel): ViewModel

}