package com.jerry.companies.inject

import com.jerry.companies.service.CompaniesAPI
import com.jerry.companies.service.adapters.BigDecimalAdapter
import com.jerry.companies.service.adapters.LocalDateTimeAdapter
import com.squareup.moshi.Moshi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {

    single {
        Moshi.Builder()
            .add(LocalDateTimeAdapter())
            .add(BigDecimalAdapter())
            .build()
    }

    single { MoshiConverterFactory.create(get()).asLenient() }

    single {
        // The json from a remote file in order to simulate a server response
        Retrofit.Builder()
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl("https://raw.githubusercontent.com/DivvyPayHQ/BusinessIntelligence/cb0bbbd65cf5804b40018f6dbe013229d9bd1d6c/")
            .build()
    }
    single { get<Retrofit>().create(CompaniesAPI::class.java) }
}