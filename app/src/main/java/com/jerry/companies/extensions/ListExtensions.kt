package com.jerry.companies.extensions

import com.jerry.companies.cache.CompaniesDataSourceFactory

fun <T> List<T>.pagedGroup(
    firstPage: Int,
    lastPage: Int = firstPage,
    pageSize: Int = CompaniesDataSourceFactory.PAGE_SIZE,
    filter: Any? = null
) = run {
    val start = firstPage * pageSize
    slice(
        start until (start + (((lastPage - firstPage) + 1) * pageSize)).coerceAtMost(
            size
        )
    ).filter { it != filter }
}