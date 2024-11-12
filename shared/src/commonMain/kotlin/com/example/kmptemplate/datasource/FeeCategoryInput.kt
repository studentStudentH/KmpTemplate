package com.example.kmptemplate.datasource

import kotlinx.datetime.Instant

/**
 * idが無いFeeCategory
 * 新規作成時に利用する
 */
data class FeeCategoryInput(
    val name: String,
    val lastUsedAt: Instant,
)
