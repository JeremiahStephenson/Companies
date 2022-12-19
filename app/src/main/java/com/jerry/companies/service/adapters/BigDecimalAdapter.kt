package com.jerry.companies.service.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.math.BigDecimal

class BigDecimalAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): BigDecimal? =
        when (reader.peek() == JsonReader.Token.NULL) {
            true -> reader.nextNull()
            else -> BigDecimal(reader.nextString())
        }

    @ToJson
    fun toJson(writer: JsonWriter, value: BigDecimal?) {
        writer.value(value)
    }
}