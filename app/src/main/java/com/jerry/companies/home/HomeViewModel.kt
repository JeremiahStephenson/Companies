package com.jerry.companies.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jerry.companies.repositories.CompanyRepository
import com.jerry.companies.service.DataResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

class HomeViewModel(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    val companiesFlow = companyRepository
        .companiesFlow
        .cachedIn(viewModelScope)

    private val _flow = MutableStateFlow<Unit?>(null)
    val flow = _flow.transformLatest {
        if (it == null) {
            emit(DataResource.done(null))
            return@transformLatest
        }
        emit(DataResource.loading())
        try {
            companyRepository.loadCompanies()
            emit(DataResource.done(Unit))
        } catch (t : Throwable) {
            emit(DataResource.error(t))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        DataResource.idle()
    )

    fun refresh() {
        _flow.value = null
        _flow.value = Unit
    }
}