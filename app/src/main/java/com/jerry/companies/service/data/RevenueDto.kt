package com.jerry.companies.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class RevenueDto(
    val seq: Int,
    val date: LocalDateTime,
    val value: BigDecimal
) : Serializable