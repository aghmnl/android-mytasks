package com.followapp.mytasks.homeModule.di

import com.followapp.mytasks.homeModule.model.HomeRepository
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase
import com.followapp.mytasks.homeModule.viewModel.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRoomDatabase)
    singleOf(::HomeRepository)
    viewModelOf(::HomeViewModel)
}