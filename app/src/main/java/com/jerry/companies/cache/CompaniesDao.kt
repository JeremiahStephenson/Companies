package com.jerry.companies.cache

import androidx.paging.PagingSource
import androidx.room.*
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.cache.data.Revenue
import kotlinx.coroutines.flow.Flow

@Dao
interface CompaniesDao {

    @Query("DELETE FROM companies WHERE timestamp < :timestamp")
    suspend fun deleteOldCompanies(timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCompanies(companies: List<Company>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRevenue(revenueList: List<Revenue>)

    @Query("SELECT * FROM companies ORDER BY companies.name")
    fun getCompaniesByName(): PagingSource<Int, Company>

    @Query("SELECT * FROM companies ORDER BY companies.id")
    fun getCompaniesById(): PagingSource<Int, Company>

    @Transaction
    @Query("SELECT * FROM companies WHERE id = :id")
    fun getCompanyFlowById(id: Long): Flow<CompanyAndRevenue>
}