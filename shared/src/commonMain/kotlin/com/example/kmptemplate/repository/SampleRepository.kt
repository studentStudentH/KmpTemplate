package com.example.kmptemplate.repository

import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.ListHolder
import com.example.kmptemplate.util.KermitLogger
import com.example.kmptemplate.util.dateFormat
import com.example.kmptemplate.util.dateTimeFormat
import com.example.kmptemplate.util.getCurrentTime
import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.coroutines.delay
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface SampleRepository {
    suspend fun getSampleWords(): KmpResult<ListHolder<String>>
}

internal class DevSampleRepositoryImpl : SampleRepository {
    override suspend fun getSampleWords(): KmpResult<ListHolder<String>> {
        KermitLogger.d(TAG) { "getSampleWords()" }
        val localDateTime = getCurrentTime().toSystemLocalDateTime()
        KermitLogger.d(TAG) { "date time = ${localDateTime.dateTimeFormat()}" }
        KermitLogger.d(TAG) { "date = ${localDateTime.dateFormat()}" }
        // 通信遅延の表現
        delay(5.0.toDuration(DurationUnit.SECONDS))
        val tempList = listOf("Dev A", "Dev B", "Dev C")
        return KmpResult.Success(ListHolder(tempList))
    }

    private companion object {
        const val TAG = "DevSampleRepositoryImpl"
    }
}

internal class ReleaseSampleRepositoryImpl : SampleRepository {
    override suspend fun getSampleWords(): KmpResult<ListHolder<String>> {
        KermitLogger.d(TAG) { "getSampleWords()" }
        // 通信遅延の表現
        delay(5.0.toDuration(DurationUnit.SECONDS))
        val tempList = listOf("Release A", "Release B", "Release C")
        return KmpResult.Success(ListHolder(tempList))
    }

    private companion object {
        const val TAG = "ReleaseSampleRepositoryImpl"
    }
}
