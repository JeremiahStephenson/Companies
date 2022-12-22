package com.jerry.companies.repositories

import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun loadCompanies()
    fun getCompanyFlow(id: Long): Flow<CompanyAndRevenue>
    suspend fun findAllCompaniesIn(ids: List<Long>): List<Company>
    suspend fun getAllIds(sort: Sort): List<Long>
}