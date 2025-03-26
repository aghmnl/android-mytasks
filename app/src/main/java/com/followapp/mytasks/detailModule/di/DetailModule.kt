package com.followapp.mytasks.detailModule.di

import com.followapp.mytasks.detailModule.model.DetailRepository
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase
import com.followapp.mytasks.detailModule.viewModel.DetailViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailModule = module {
    singleOf(::DetailRoomDatabase)
    singleOf(::DetailRepository)
    viewModelOf(::DetailViewModel)
}