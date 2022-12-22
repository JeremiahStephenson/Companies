package com.jerry.companies

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jerry.companies.cache.CompaniesDatabase
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.Revenue
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.CompanyRepositoryImpl
import com.jerry.companies.service.CompaniesAPI
import com.jerry.companies.util.CoroutineContextProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigDecimal
import java.time.Instant
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class CompanyRepositoryImplTest : KoinComponent {

    private val companiesApi by inject<CompaniesAPI>()
    private val companiesDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CompaniesDatabase::class.java
    ).setTransactionExecutor(Executors.newSingleThreadExecutor())
        .build()
    private val companiesDao = companiesDatabase.companiesDao()

    private lateinit var companyRepository: CompanyRepository

    @Before
    fun setUp() {
        companyRepository = CompanyRepositoryImpl(
            companiesApi,
            companiesDao,
            companiesDatabase,
            CoroutineContextProvider.MainCoroutineContext
        )
    }

    @After
    fun tearDown() {
        companiesDatabase.close()
    }

    @Test
    fun testLocalCache() = runTest {
        val companyId = 0L
        companiesDao.insertAllCompanies(
            listOf(
                Company(
                    companyId,
                    "company",
                    "address",
                    "city",
                    "country",
                    Instant.now()
                )
            )
        )

        companiesDao.insertAllRevenue(
            listOf(
                Revenue(
                    companyId,
                    0,
                    Instant.now(),
                    BigDecimal.valueOf(454313.12)
                ),
                Revenue(
                    companyId,
                    1,
                    Instant.now(),
                    BigDecimal.valueOf(879789.12)
                )
            )
        )

        // Make sure things got saved
        val company = companiesDao.findCompanyById(companyId)
        assert(company != null)

        val revenue = companiesDao.findAllRevenue(companyId)
        assert(revenue.size == 2)

        // Now delete and make sure the delete cascades down
        companiesDao.deleteCompanyById(companyId)

        val noCompany = companiesDao.findCompanyById(companyId)
        assert(noCompany == null)

        val emptyRevenue = companiesDao.findAllRevenue(companyId)
        assert(emptyRevenue.isEmpty())
    }
}