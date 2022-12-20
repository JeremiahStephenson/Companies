package com.jerry.companies.repositories

import androidx.paging.PagingData
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.service.DataResource
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    val remoteCompaniesFlow: Flow<DataResource<Unit>>
    suspend fun loadCompanies()
    fun getLocalCompaniesFlow(sort: Sort): Flow<PagingData<Company>>
    fun getCompanyFlow(id: Long): Flow<CompanyAndRevenue>
}