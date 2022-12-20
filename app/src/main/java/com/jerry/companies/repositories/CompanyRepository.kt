package com.jerry.companies.repositories

import androidx.paging.PagingData
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.service.DataResource
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun loadCompanies()
    val remoteCompaniesFlow: Flow<DataResource<Unit>>
    fun getLocalCompaniesFlow(filter: Filter): Flow<PagingData<CompanyAndRevenue>>
}