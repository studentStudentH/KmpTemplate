package com.example.kmptemplate.domainmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * @param name: カテゴリ名
 * @param lastUsedAt: 最後に使われた日時
 */
@Entity
data class FeeCategory(
    @PrimaryKey(autoGenerate = false) val name: String,
    val lastUsedAt: Instant,
)
