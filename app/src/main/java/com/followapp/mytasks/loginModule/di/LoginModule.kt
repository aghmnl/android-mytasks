package com.followapp.mytasks.loginModule.di

import com.followapp.mytasks.common.dataAccess.services.FirebaseService
import com.followapp.mytasks.common.dataAccess.services.GooglePlayService
import com.followapp.mytasks.loginModule.model.LoginRepository
import com.followapp.mytasks.loginModule.viewModel.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginModule = module {
    singleOf(::GooglePlayService)
    singleOf(::FirebaseService)
    singleOf(::LoginRepository)
    viewModelOf(::LoginViewModel)
}