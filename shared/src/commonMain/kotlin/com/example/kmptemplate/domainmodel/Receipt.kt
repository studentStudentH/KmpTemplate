package com.example.kmptemplate.domainmodel

import kotlinx.datetime.Instant

/**
 * @param id: データのid。自動瀬性するのでクライアントで指定する必要は無い想定
 * @param cost: 何円かかったか
 * @param category: 何に支払ったか
 * @param createdAt: いつに支払ったか
 */
data class Receipt
    @Throws(IllegalArgumentException::class)
    constructor(
        val id: Int = 0,
        val cost: Int,
        val category: String,
        val createdAt: Instant,
    ) {
        init {
            if (cost < 0) throw IllegalArgumentException("costは0以上にしてください")
        }
    }
