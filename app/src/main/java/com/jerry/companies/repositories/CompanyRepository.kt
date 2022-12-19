package com.jerry.companies.repositories

import androidx.paging.PagingData
import com.jerry.companies.cache.data.CompanyAndRevenue
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun loadCompanies()
    val companiesFlow: Flow<PagingData<CompanyAndRevenue>>
}