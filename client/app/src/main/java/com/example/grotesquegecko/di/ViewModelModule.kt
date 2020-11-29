package com.example.grotesquegecko.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import com.example.grotesquegecko.ui.addnewcaff.AddNewCaffViewModel
import com.example.grotesquegecko.ui.addnewcomment.AddNewCommentViewModel
import com.example.grotesquegecko.ui.allusers.AllUsersViewModel
import com.example.grotesquegecko.ui.blank.BlankViewModel
import com.example.grotesquegecko.ui.caffdetails.CaffDetailsViewModel
import com.example.grotesquegecko.ui.caffsearcher.CaffSearcherViewModel
import com.example.grotesquegecko.ui.login.LoginViewModel
import com.example.grotesquegecko.ui.userdata.UserDataViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CaffSearcherViewModel::class)
    abstract fun bindCaffSearcherViewModel(caffSearcherViewModel: CaffSearcherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CaffDetailsViewModel::class)
    abstract fun bindCaffDetailsViewModel(caffDetailsViewModel: CaffDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNewCaffViewModel::class)
    abstract fun bindAddNewCaffViewModel(addNewCaffViewModel: AddNewCaffViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNewCommentViewModel::class)
    abstract fun bindAddNewCommentViewModel(addNewCommentViewModel: AddNewCommentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDataViewModel::class)
    abstract fun bindUserDataViewModel(userDataViewModel: UserDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllUsersViewModel::class)
    abstract fun bindAllUsersViewModel(allUsersViewModel: AllUsersViewModel): ViewModel
}
