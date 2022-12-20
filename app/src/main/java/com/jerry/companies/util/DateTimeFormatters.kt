package com.jerry.companies.util

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

const val LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
const val READABLE_DATE_PATTERN = "MM/yy"

val READABLE_DATE_PATTERN_PARSER = DateTimeFormatter.ofPattern(
    READABLE_DATE_PATTERN,
    Locale.US
).withZone(ZoneOffset.UTC)

val LOCAL_DATE_TIME_UTC_PARSER: DateTimeFormatter = DateTimeFormatter.ofPattern(
    LOCAL_DATE_TIME_PATTERN,
    Locale.US
).withZone(ZoneOffset.UTC)