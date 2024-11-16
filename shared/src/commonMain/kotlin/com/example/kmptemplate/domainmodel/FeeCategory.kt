package com.example.kmptemplate.domainmodel

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * @param id: カテゴリのID。自動採番される
 * @param name: カテゴリ名
 * @param lastUsedAt: 最後に使われた日時
 */
@Entity(indices = [Index(value = ["name"], unique = true)])
data class FeeCategory(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val lastUsedAt: Instant,
) {
    internal companion object {
        val INITIAL_CATEGORIES = listOf("食費", "交通費", "通信費", "その他")
    }
}
