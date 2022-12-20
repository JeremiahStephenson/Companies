package com.jerry.companies.home

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.jerry.companies.cache.data.Company
import com.jerry.companies.repositories.Sort
import com.jerry.companies.service.DataResource
import androidx.compose.runtime.*

@Stable
class HomeState(
    val dataPaging: LazyPagingItems<Company>,
    loadingState: State<DataResource<Unit>?>,
    sortState: State<Sort>
) {
    val loading by loadingState
    val sort by sortState
}