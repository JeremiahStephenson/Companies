package com.jerry.companies.inject

import com.jerry.companies.cache.CompaniesDataSourceFactory
import com.jerry.companies.details.DetailsViewModel
import com.jerry.companies.home.HomeViewModel
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.CompanyRepositoryImpl
import com.jerry.companies.util.CoroutineContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CompanyRepository> { CompanyRepositoryImpl(get(), get(), get(), get()) }

    single<CoroutineContextProvider> { CoroutineContextProvider.MainCoroutineContext }

    factory { CompaniesDataSourceFactory(get(), get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get(), get()) }
}