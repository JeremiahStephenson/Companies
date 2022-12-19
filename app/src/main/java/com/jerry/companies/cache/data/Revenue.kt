package com.jerry.companies.cache.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.Instant

@Entity(
    tableName = Revenue.TABLE_NAME,
    indices = [(Index(value = ["id", "companyId"], unique = true))],
    foreignKeys = [(ForeignKey(
        entity = Company::class,
        parentColumns = ["id"],
        childColumns = ["companyId"],
        onDelete = ForeignKey.CASCADE
    ))]
)
data class Revenue(
    var companyId: Long = 0L,
    var seq: Int = 0,
    var date: Instant? = null,
    var value: BigDecimal = BigDecimal.ZERO
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    companion object {
        const val TABLE_NAME = "revenue"
    }
}