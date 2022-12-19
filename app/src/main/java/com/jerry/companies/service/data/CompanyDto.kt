package com.jerry.companies.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CompanyDto(
    val id: Long,
    val name: String,
    val location: LocationDto,
    val revenue: List<RevenueDto>
) : Serializable