package com.example.kmptemplate.domainmodel

import kotlinx.datetime.Instant

/**
 * @param name: カテゴリ名
 * @param lastUsedAt: 最後に使われた日時
 */
data class FeeCategory(
    val name: String,
    val lastUsedAt: Instant,
)
