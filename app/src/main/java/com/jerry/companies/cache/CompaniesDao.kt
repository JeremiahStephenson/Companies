package com.jerry.companies.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.cache.data.Revenue

@Dao
interface CompaniesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCompanies(companies: List<Company>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRevenue(revenueList: List<Revenue>)

    @Transaction
    @Query("SELECT * FROM companies")
    fun getCompaniesWithRevenue(): PagingSource<Int, CompanyAndRevenue>
}