package com.jerry.companies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jerry.companies.destinations.DetailsMainDestination
import com.jerry.companies.repositories.CompanyRepository
import kotlinx.coroutines.flow.filterNotNull

class DetailsViewModel(
    private val handle: SavedStateHandle,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    val companyFlow =
        companyRepository.getCompanyFlow(
            DetailsMainDestination.argsFrom(handle).companyId
        ).filterNotNull()
}