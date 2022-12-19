package com.jerry.companies.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.jerry.companies.cache.CompaniesDao
import com.jerry.companies.cache.CompaniesDatabase
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.Revenue
import com.jerry.companies.service.CompaniesAPI
import java.time.ZoneOffset

class CompanyRepositoryImpl(
    private val companiesAPI: CompaniesAPI,
    private val companiesDao: CompaniesDao,
    private val companiesDatabase: CompaniesDatabase
) : CompanyRepository {
    override suspend fun loadCompanies() {
        val list = companiesAPI.getCompanyList()

        companiesDatabase.withTransaction {
            val companies = list.map {
                Company(
                    it.id,
                    it.name,
                    it.location.address,
                    it.location.city,
                    it.location.country
                )
            }
            companiesDao.insertAllCompanies(companies)

            val revenueList = list.flatMap { company ->
                company.revenue.map { revenue ->
                    Revenue(
                        company.id,
                        revenue.seq,
                        revenue.date.toInstant(ZoneOffset.UTC),
                        revenue.value
                    )
                }
            }
            companiesDao.insertAllRevenue(revenueList)
        }
    }

    override val companiesFlow = Pager(PagingConfig(40)) {
        companiesDao.getCompaniesWithRevenue()
    }.flow
}