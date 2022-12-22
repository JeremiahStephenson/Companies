package com.jerry.companies.repositories

import androidx.room.withTransaction
import com.jerry.companies.cache.CompaniesDao
import com.jerry.companies.cache.CompaniesDatabase
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.Revenue
import com.jerry.companies.service.CompaniesAPI
import com.jerry.companies.util.CoroutineContextProvider
import java.time.Instant
import java.time.ZoneOffset

class CompanyRepositoryImpl(
    private val companiesAPI: CompaniesAPI,
    private val companiesDao: CompaniesDao,
    private val companiesDatabase: CompaniesDatabase,
    cc: CoroutineContextProvider
) : CompanyRepository {

    override suspend fun loadCompanies() {

        // Load the list from the remote source
        val list = companiesAPI.getCompanyList()

        // Save it all to the local database
        companiesDatabase.withTransaction {
            // keep track of when we loaded these so
            // we can delete old ones that don't returned in the future
            val now = Instant.now()
            val companies = list.map {
                Company(
                    it.id,
                    it.name,
                    it.location.address,
                    it.location.city,
                    it.location.country,
                    now
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

            // Now delete any old items that are not being returned any more
            companiesDao.deleteOldCompanies(now.toEpochMilli())
        }
    }

    // Returns a flow for a company with its revenue
    override fun getCompanyFlow(id: Long) = companiesDao.getCompanyFlowById(id)

    override suspend fun findAllCompaniesIn(ids: List<Long>): List<Company> {
        return companiesDao.findAllCompaniesIn(ids)
    }

    override suspend fun getAllIds(sort: Sort): List<Long> {
        return when (sort) {
            Sort.NAME -> companiesDao.getAllIdsSortByName()
            Sort.ID -> companiesDao.getAllIdsSortById()
        }
    }
}