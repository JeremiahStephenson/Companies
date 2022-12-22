package com.jerry.companies.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jerry.companies.cache.data.Company
import com.jerry.companies.extensions.pagedGroup
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.Sort
import com.jerry.companies.util.CoroutineContextProvider
import kotlinx.coroutines.withContext
import kotlin.math.ceil

class CompaniesDataSourceFactory(
    private val companyRepository: CompanyRepository,
    private val cc: CoroutineContextProvider
) {

    private var dataSource: CompaniesDataSource? = null
    private var currentSort: Sort? = null

    fun generateDataSource(
        sort: Sort = DEFAULT_SORT,
        onError: (Throwable) -> Unit
    ): CompaniesDataSource {
        val loadRemote = currentSort == null || currentSort == sort
        currentSort = sort
        return CompaniesDataSource(loadRemote, sort, onError).also {
            dataSource = it
        }
    }

    inner class CompaniesDataSource(
        private val loadRemote: Boolean = true,
        private val sort: Sort = DEFAULT_SORT,
        private val onError: (Throwable) -> Unit
    ) : PagingSource<Int, Company>() {

        private var companyIds = emptyList<Long>()

        override fun getRefreshKey(state: PagingState<Int, Company>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Company> {
            val page = params.key ?: 0
            val loadSize = params.loadSize
            return withContext(cc.io) {
                try {
                    if (companyIds.isEmpty() && page == 0) {
                        if (loadRemote) {
                            companyRepository.loadCompanies()
                        }
                        fillInCompanyIds()
                    }
                } catch (t: Throwable) {
                    fillInCompanyIds()
                    if (companyIds.isEmpty()) {
                        return@withContext LoadResult.Error(t)
                    }
                    onError(t)
                }
                val ids = companyIds.pagedGroup(
                    firstPage = page,
                    pageSize = loadSize
                )
                val totalCount =
                    ceil((companyIds.size).toDouble() / loadSize.toDouble()).toInt()

                val companies = companyRepository.findAllCompaniesIn(
                    ids
                ).sortedWith { company1, company2 ->
                    when {
                        ids.indexOf(company1.id) < ids.indexOf(company2.id) -> -1
                        else -> 1
                    }
                }
                LoadResult.Page(
                    data = companies,
                    prevKey = if (page > 0) page - 1 else null,
                    nextKey = if (page + 1 in 0 until totalCount) page + 1 else null
                )
            }
        }

        private suspend fun fillInCompanyIds() {
            companyIds = companyRepository.getAllIds(sort)
        }
    }

    companion object {
        const val PAGE_SIZE = 50

        private val DEFAULT_SORT = Sort.ID
    }
}