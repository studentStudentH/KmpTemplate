package com.example.kmptemplate.domainmodel

import kotlinx.datetime.Instant

/**
 * @param cost: 何円かかったか
 * @param category: 何に支払ったか
 * @param createdAt: いつに支払ったか
 */
data class Receipt
    @Throws(IllegalArgumentException::class)
    constructor(
        val cost: Int,
        val category: String,
        val createdAt: Instant,
    ) {
        init {
            if (cost < 0) throw IllegalArgumentException("costは0以上にしてください")
        }
    }
