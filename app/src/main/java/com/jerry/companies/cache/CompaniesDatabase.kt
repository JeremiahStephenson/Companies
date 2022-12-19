package com.jerry.companies.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jerry.companies.cache.adapters.BigDecimalDoubleTypeConverter
import com.jerry.companies.cache.adapters.InstantLongConverter
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.Revenue

@Database(
    entities = [Company::class, Revenue::class], version = 1, exportSchema = false
)
@TypeConverters(InstantLongConverter::class, BigDecimalDoubleTypeConverter::class)
abstract class CompaniesDatabase : RoomDatabase() {
    abstract fun companiesDao(): CompaniesDao
    companion object {
        const val DATABASE_NAME = "companiesDb"
    }
}