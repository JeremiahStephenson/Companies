package com.jerry.companies.cache.adapters

import androidx.room.TypeConverter
import java.time.Instant

class InstantLongConverter {
    @TypeConverter
    fun fromLongToInstant(value: Long?): Instant? = value?.let {
        when (it >= 0) {
            true -> Instant.ofEpochMilli(it)
            else -> MIN_INSTANT
        }
    }

    @TypeConverter
    fun fromInstantToLong(value: Instant?): Long? = value?.toEpochMilli()

    companion object {
        private val MIN_INSTANT = Instant.ofEpochSecond(0)
    }
}