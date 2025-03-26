package com.followapp.mytasks.adModule.di

import com.followapp.mytasks.adModule.model.AdRepository
import com.followapp.mytasks.adModule.viewModel.AdViewModel
import com.followapp.mytasks.common.dataAccess.services.GoogleAdService
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val adModule = module {
    singleOf(::GoogleAdService)
    singleOf(::AdRepository)
    viewModelOf(::AdViewModel)
}