package com.jerry.companies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerry.companies.destinations.DetailsMainDestination
import com.jerry.companies.repositories.CompanyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    handle: SavedStateHandle,
    companyRepository: CompanyRepository
) : ViewModel() {

    val companyFlow =
        companyRepository.getCompanyFlow(DetailsMainDestination.argsFrom(handle).companyId)
            .filterNotNull()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                null)
}