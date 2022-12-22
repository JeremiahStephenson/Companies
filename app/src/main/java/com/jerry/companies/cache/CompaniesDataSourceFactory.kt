package com.jerry.companies.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jerry.companies.cache.data.Company
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.util.CoroutineContextProvider
import kotlinx.coroutines.withContext
import kotlin.math.ceil

class CompaniesDataSourceFactory(
    private val companyRepository: CompanyRepository,
    private val cc: CoroutineContextProvider
) {

    private var dataSource: CompaniesDataSource? = null

    fun generateDataSource(onError: (Throwable) -> Unit): CompaniesDataSource {
        return CompaniesDataSource(onError).also {
            dataSource = it
        }
    }

    inner class CompaniesDataSource(
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
                        companyIds = companyRepository.loadCompanies()
                    }
                } catch (t: Throwable) {
                    companyIds = companyRepository.getAllIds()
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
                val companies = companyRepository.findAllCompaniesIn(ids).shuffled()
                LoadResult.Page(
                    data = companies,
                    prevKey = if (page > 0) page - 1 else null,
                    nextKey = if (page + 1 in 0 until totalCount) page + 1 else null
                )
            }
        }

        private fun <T> List<T>.pagedGroup(
            firstPage: Int,
            lastPage: Int = firstPage,
            pageSize: Int = PAGE_SIZE,
            filter: Any? = null
        ) = run {
            val start = firstPage * pageSize
            slice(
                start until (start + (((lastPage - firstPage) + 1) * pageSize)).coerceAtMost(
                    size
                )
            ).filter { it != filter }
        }
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}