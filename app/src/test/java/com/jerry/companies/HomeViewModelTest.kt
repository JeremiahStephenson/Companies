package com.jerry.companies

import app.cash.turbine.test
import com.jerry.companies.home.HomeViewModel
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.service.DataResource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private val companyRepository = mockk<CompanyRepository>()

    @get:Rule
    val testRule = MainTestRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(companyRepository)
    }

    @Test
    fun testFlowSuccess() = runTest {

        val flow = MutableStateFlow<DataResource<Unit>>(DataResource.idle())

        every { companyRepository.remoteCompaniesFlow } returns flow

        viewModel.loadingFlow.test {
            val idle = awaitItem()
            assert(idle?.isIdle == true)

            flow.value = DataResource.loading()
            val loading = awaitItem()
            assert(loading?.isLoading == true)

            flow.value = DataResource.done(Unit)
            val success = awaitItem()
            assert(success?.isSuccessful == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testFlowFail() = runTest {
        val flow = MutableStateFlow<DataResource<Unit>>(DataResource.idle())

        every { companyRepository.remoteCompaniesFlow } returns flow

        viewModel.loadingFlow.test {
            val idle = awaitItem()
            assert(idle?.isIdle == true)

            flow.value = DataResource.error(Throwable())
            val error = awaitItem()
            assert(error?.isError == true)

            viewModel.refresh()

            flow.value = DataResource.done(Unit)

            val success = awaitItem()
            assert(success?.isSuccessful == true)

            cancelAndIgnoreRemainingEvents()
        }
    }
}