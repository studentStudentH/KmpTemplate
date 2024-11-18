package com.example.kmptemplate.domainmodel

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @param id: データのid
 * @param cost: 何円かかったか
 * @param category: 何に支払ったか。nullは値が設定されていないことを意味する
 * @param createdAt: いつに支払ったか
 */
data class Receipt
    @Throws(IllegalArgumentException::class)
    constructor(
        val id: String,
        val cost: Int,
        val category: FeeCategory?,
        val createdAt: Instant,
    ) {
        init {
            if (cost < 0) throw IllegalArgumentException("costは0以上にしてください")
        }

        companion object {
            @OptIn(ExperimentalUuidApi::class)
            fun makeInstanceForPreview(
                cost: Int,
                categoryName: String
            ): Receipt {
                val pseudoInstant = Clock.System.now()
                val pseudoId = Uuid.random().toHexString()
                return Receipt(
                    id = pseudoId,
                    cost = cost,
                    category = FeeCategory(
                        id = pseudoId,
                        name = categoryName,
                        lastUsedAt = pseudoInstant
                    ),
                    createdAt = pseudoInstant
                )
            }
        }
    }
