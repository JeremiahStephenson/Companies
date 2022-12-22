package com.jerry.companies.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jerry.companies.cache.CompaniesDataSourceFactory
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.Sort
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val companyRepository: CompanyRepository,
    private val companyDataSource: CompaniesDataSourceFactory
) : ViewModel() {

    private val _errorFlow = MutableStateFlow(false)
    val errorFlow = _errorFlow.asStateFlow()

    val companiesPager = Pager(PagingConfig(pageSize = 40)) {
        companyDataSource.generateDataSource {
            _errorFlow.value = true
        }
    }.flow.cachedIn(viewModelScope)

    fun dismissErrorDialog() {
        _errorFlow.value = false
    }

    private val _sortFlow = MutableStateFlow(Sort.ID)
    val sortFlow = _sortFlow.asStateFlow()

    val localCompaniesFlow = sortFlow.flatMapLatest {
        companyRepository.getLocalCompaniesFlow(it)
    }.cachedIn(viewModelScope)

    // We can use this flow to trigger a refresh
    // It might be better to use enums for this but this works fine
    // for this demo
    private val _loadingFlow = MutableStateFlow<Unit?>(Unit)
    val loadingFlow = _loadingFlow.filterNotNull().flatMapLatest {
        companyRepository.remoteCompaniesFlow
    }
    .onEach {
        _loadingFlow.value = null
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    fun setSort(sort: Sort) {
        _sortFlow.value = sort
    }

//    fun refresh() {
//        companyDataSource.invalidate()
//        //_loadingFlow.value = Unit
//    }
}