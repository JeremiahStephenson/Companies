package com.jerry.companies.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class LocationDto(
    val address: String,
    val city: String,
    val country: String
) : Serializable