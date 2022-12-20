package com.jerry.companies.cache.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = Company.TABLE_NAME
)
data class Company(
    @PrimaryKey val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val timestamp: Instant
) {
    companion object {
        const val TABLE_NAME = "companies"
    }
}