package com.example.kmptemplate.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }

    @TypeConverter
    fun instantToString(value: Instant): Long {
        return value.toEpochMilliseconds()
    }
}
