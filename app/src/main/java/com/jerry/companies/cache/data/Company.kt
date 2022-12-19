package com.jerry.companies.cache.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = Company.TABLE_NAME
)
data class Company(
    @PrimaryKey val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val country: String,
) {
    companion object {
        const val TABLE_NAME = "companies"
    }
}