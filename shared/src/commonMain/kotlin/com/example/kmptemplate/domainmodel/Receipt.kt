package com.example.kmptemplate.domainmodel

import kotlinx.datetime.Instant

data class Receipt @Throws(IllegalArgumentException::class) constructor(
    val cost: Int, // 何円かかったか
    val category: String, // 何に支払ったか
    val createdAt: Instant, // いつに支払ったか
) {
    init {
        if (cost < 0) throw IllegalArgumentException("costは0以上にしてください")
    }
}
