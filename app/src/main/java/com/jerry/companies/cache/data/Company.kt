package com.jerry.companies.cache.data

import android.content.Intent
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URLEncoder
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

    private val fullAddress get() = "$address, $city, $country"

    val mapIntent get() = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.google.com/maps/search/?api=1&query=${
            URLEncoder.encode(fullAddress, Charsets.UTF_8.name())
        }")
    )
}