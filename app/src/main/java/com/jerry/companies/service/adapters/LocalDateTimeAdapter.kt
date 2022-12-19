package com.jerry.companies.service.adapters

import com.jerry.companies.util.LOCAL_DATE_TIME_UTC_PARSER
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(localDate: LocalDateTime): String =
            localDate.format(LOCAL_DATE_TIME_UTC_PARSER)

    @FromJson
    fun fromJson(json: String): LocalDateTime =
            LocalDateTime.parse(json, LOCAL_DATE_TIME_UTC_PARSER)
}