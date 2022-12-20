package com.jerry.companies.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.jerry.companies.cache.CompaniesDao
import com.jerry.companies.cache.CompaniesDatabase
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.CompanyAndRevenue
import com.jerry.companies.cache.data.Revenue
import com.jerry.companies.service.CompaniesAPI
import com.jerry.companies.service.DataResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.time.Instant
import java.time.ZoneOffset

class CompanyRepositoryImpl(
    private val companiesAPI: CompaniesAPI,
    private val companiesDao: CompaniesDao,
    private val companiesDatabase: CompaniesDatabase
) : CompanyRepository {

    override val remoteCompaniesFlow = flow {
        loadCompanies()
        emit(DataResource.done(Unit))
    }.onStart {
        emit(DataResource.loading())
    }.catch {
        emit(DataResource.error(it))
    }

    override suspend fun loadCompanies() {
        val list = companiesAPI.getCompanyList()

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

    override fun getLocalCompaniesFlow(sort: Sort): Flow<PagingData<Company>> {
        return Pager(PagingConfig(40)) {
            when (sort) {
                Sort.NAME -> companiesDao.getCompaniesByName()
                Sort.ID -> companiesDao.getCompaniesById()
            }
        }.flow
    }

    override fun getCompanyFlow(id: Long): Flow<CompanyAndRevenue> {
        return companiesDao.getCompanyFlowById(id)
    }
}