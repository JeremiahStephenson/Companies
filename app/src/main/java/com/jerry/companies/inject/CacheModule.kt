package com.jerry.companies.inject

import androidx.room.Room
import com.jerry.companies.cache.CompaniesDatabase
import org.koin.dsl.module

val cacheModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CompaniesDatabase::class.java,
            CompaniesDatabase.DATABASE_NAME
        ).build()
    }
    single { get<CompaniesDatabase>().companiesDao() }
}