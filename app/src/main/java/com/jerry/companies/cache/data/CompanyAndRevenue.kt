package com.jerry.companies.cache.data

import androidx.room.Embedded
import androidx.room.Relation

data class CompanyAndRevenue(
    @Embedded val company: Company,
    @Relation(
        parentColumn = "id",
        entityColumn = "companyId"
    )
    val revenue: List<Revenue>
)