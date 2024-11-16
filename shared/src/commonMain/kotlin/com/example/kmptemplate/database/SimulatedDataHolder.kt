package com.example.kmptemplate.database

import com.example.kmptemplate.domainmodel.FeeCategory
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Debug環境でオンメモリでデータを保存するためのクラス
 */
internal class SimulatedDataHolder(
    var feeCategoryList: List<FeeCategory>,
    var receiptList: List<RoomReceipt>,
) {
    companion object {
        /**
         * 単体テスト用のデータを生成する
         */
        @OptIn(ExperimentalUuidApi::class)
        fun makeTestData(): SimulatedDataHolder {
            val feeCategories =
                FeeCategory.INITIAL_CATEGORIES.map {
                    val id = Uuid.random().toHexString()
                    FeeCategory(id, it, Clock.System.now())
                }
            val roomReceipts =
                feeCategories.map {
                    val id = Uuid.random().toHexString()
                    val cost = 1000
                    RoomReceipt(id, cost, it.id, Clock.System.now())
                }
            return SimulatedDataHolder(
                feeCategories,
                roomReceipts,
            )
        }
    }
}
