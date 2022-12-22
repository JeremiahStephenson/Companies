package com.jerry.companies.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jerry.companies.cache.CompaniesDataSourceFactory
import com.jerry.companies.repositories.Sort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class HomeViewModel(
    private val companyDataSource: CompaniesDataSourceFactory
) : ViewModel() {

    private val _currentSortFlow = MutableStateFlow(Sort.ID)
    val currentSortFlow = _currentSortFlow.asStateFlow()

    private val _errorFlow = MutableStateFlow(false)
    val errorFlow = _errorFlow.asStateFlow()

    val companiesPager = _currentSortFlow.flatMapLatest {
        Pager(PagingConfig(pageSize = 40)) {
            companyDataSource.generateDataSource(it) {
                _errorFlow.value = true
            }
        }.flow
    }.cachedIn(viewModelScope)

    fun dismissErrorDialog() {
        _errorFlow.value = false
    }

    fun setSort(sort: Sort) {
        _currentSortFlow.value = sort
    }

}