package com.jerry.companies.inject

import com.jerry.companies.home.HomeViewModel
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.CompanyRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CompanyRepository> { CompanyRepositoryImpl(get(), get(), get()) }

    viewModel { HomeViewModel(get()) }
}