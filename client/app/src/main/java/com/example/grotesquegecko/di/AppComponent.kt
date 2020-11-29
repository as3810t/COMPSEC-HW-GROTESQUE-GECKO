package com.example.grotesquegecko.di

import android.content.Context
import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import com.example.grotesquegecko.MainActivity
import com.example.grotesquegecko.data.network.NetworkModule
import com.example.grotesquegecko.data.network.token.Token
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RainbowCakeModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : RainbowCakeComponent {
    fun inject(mainActivity: MainActivity)
    fun getToken(): Token

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
