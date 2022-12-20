package com.jerry.companies.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.repositories.Filter
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val filterFlow = MutableStateFlow(Filter.ID)

    val localCompaniesFlow = filterFlow.flatMapLatest {
        companyRepository.getLocalCompaniesFlow(it)
            .cachedIn(viewModelScope)
    }

    private val _uiFlow = MutableStateFlow<Unit?>(Unit)
    val uiFlow = _uiFlow.filterNotNull().flatMapLatest {
        companyRepository.remoteCompaniesFlow
    }
    .onEach {
        _uiFlow.value = null
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    fun setFilter(filter: Filter) {
        filterFlow.value = filter
    }

    fun refresh() {
        _uiFlow.value = Unit
    }
}