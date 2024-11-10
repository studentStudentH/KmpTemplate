package com.example.kmptemplate.datasource

import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpResult

interface FeeCategoryDataSource {
    suspend fun getAllCategory(): KmpResult<List<FeeCategory>>

    suspend fun addCategory(vararg feeCategories: FeeCategory): KmpResult<Unit>

    suspend fun update(vararg feeCategories: FeeCategory): KmpResult<Unit>

    suspend fun delete(vararg feeCategories: FeeCategory): KmpResult<Unit>
}
