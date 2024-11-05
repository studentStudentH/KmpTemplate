package com.example.kmptemplate.repository

import com.example.kmptemplate.domainmodel.ListHolder

interface SampleRepository {
    suspend fun getSampleSentences(): Result<ListHolder<String>>
}

internal class DevSampleRepositoryImpl: SampleRepository {
    override suspend fun getSampleSentences(): Result<ListHolder<String>> {
        val tempList = listOf("Dev A", "Dev B", "Dev C")
        return Result.success(ListHolder(tempList))
    }
}

internal class ReleaseSampleRepositoryImpl: SampleRepository {
    override suspend fun getSampleSentences(): Result<ListHolder<String>> {
        val tempList = listOf("Release A", "Release B", "Release C")
        return Result.success(ListHolder(tempList))
    }
}
