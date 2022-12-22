package com.jerry.companies.home

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.paging.compose.LazyPagingItems
import com.jerry.companies.cache.data.Company
import com.jerry.companies.repositories.Sort

@Stable
class HomeState(
    val dataPaging: LazyPagingItems<Company>,
    errorDialogState: State<Boolean>,
    sortState: State<Sort>
) {
    val errorDialog by errorDialogState
    val sort by sortState
}