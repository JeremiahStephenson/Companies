package com.jerry.companies.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.cache.data.Revenue

@Dao
interface CompaniesDao {

    @Query("DELETE FROM companies WHERE timestamp < :timestamp")
    suspend fun deleteOldCompanies(timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCompanies(companies: List<Company>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRevenue(revenueList: List<Revenue>)

    @Transaction
    @Query("SELECT * FROM companies ORDER BY companies.name")
    fun getCompaniesWithRevenueByName(): PagingSource<Int, CompanyAndRevenue>

    @Transaction
    @Query("SELECT * FROM companies ORDER BY companies.id")
    fun getCompaniesWithRevenueById(): PagingSource<Int, CompanyAndRevenue>
}