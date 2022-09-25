package com.example.there.convertermvi.data.cache.converter

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverters {
    @TypeConverter
    fun toDate(value: Long): Date = Date(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time
}