package com.example.kmptemplate

import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.Receipt
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertFails

class ReceiptTest {
    @Test
    fun test() {
        val feeCategory = FeeCategory(
            "categoryId",
            "category",
            Clock.System.now(),
        )
        val receipt =
            Receipt(
                "categoryId",
                10,
                feeCategory,
                Clock.System.now(),
            )
        assertFails("支払った値段がマイナスなのに例外を吐きませんでした") {
            receipt.copy(cost = -10)
        }
    }
}
