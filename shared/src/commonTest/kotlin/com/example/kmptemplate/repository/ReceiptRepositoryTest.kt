package com.example.kmptemplate.repository

import com.example.kmptemplate.database.RoomReceipt
import com.example.kmptemplate.database.SimulatedDataHolder
import com.example.kmptemplate.database.SimulatedFeeCategoryDao
import com.example.kmptemplate.database.SimulatedRoomReceiptDao
import com.example.kmptemplate.datasource.RoomFeeCategoryDataSource
import com.example.kmptemplate.datasource.RoomReceiptDataSource
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.YearMonth
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ReceiptRepositoryTest {
    @Test
    fun testGetAllReceipts() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)

            // when
            val result = repository.getAllReceipts()

            // then
            checkResult(result, "getAllReceipts()が失敗しています。") { receiptCollection ->
                val receipts = receiptCollection.sortByInstantDescending()
                val receiptIds = receipts.map { it.id }.sorted()
                val expectedIds = dataHolder.receiptList.map { it.id }.sorted()
                assertEquals(
                    "dataHolder内のデータとAPIから得られたデータが一致しません",
                    expected = expectedIds,
                    actual = receiptIds,
                )
            }
        }

    @Test
    fun testGetReceiptsNewerThan() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)

            // when
            val result = repository.getReceiptsNewerThan(2024, 10)

            // then
            checkResult(result, "getReceiptsNewerThan()が失敗しています。") { receiptCollection ->
                val receipts = receiptCollection.sortByInstantDescending()
                assertEquals(
                    "Receiptの件数が期待と違います",
                    expected = 2,
                    actual = receipts.size,
                )
                val yearMonth = receiptCollection.getPrevYearMonth()
                assertEquals(
                    "取得したReceiptの前の月は9月のはずです",
                    expected = 9,
                    actual = yearMonth.month,
                )
            }
        }

    @Test
    fun testGetReceiptsBetween() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)

            // when
            val result =
                repository.getReceiptsBetween(
                    oldestYear = 2024,
                    oldestMonth = 9,
                    newestYear = 2024,
                    newestMonth = 10,
                )

            // then
            checkResult(result, "getReceiptsNewerThan()が失敗しています。") { receiptCollection ->
                val receipts = receiptCollection.sortByInstantDescending()
                assertEquals(
                    "Receiptの件数が期待と違います",
                    expected = 2,
                    actual = receipts.size,
                )
                val prevYearMonth = receiptCollection.getPrevYearMonth()
                assertEquals(
                    "取得したReceiptの前の月は8月のはずです",
                    expected = 8,
                    actual = prevYearMonth.month,
                )
                val nextYearMonth = receiptCollection.getNextYearMonth()
                assertEquals(
                    "取得したReceiptの次の月は11月のはずです",
                    expected = 11,
                    actual = nextYearMonth.month,
                )
            }
        }

    @Test
    fun testAdd() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)
            val targetCategory = dataHolder.feeCategoryList.first()
            val targetCategoryLastUsedAtTime = targetCategory.lastUsedAt

            // when
            val result =
                repository.add(
                    cost = 1000,
                    category = targetCategory,
                    createdAt = Clock.System.now(),
                )

            // then
            checkResult(result, "add()が失敗しています。") { addedReceipt ->
                assertEquals(
                    "addしたのでreceiptのアイテム数は１つ増えるはずです",
                    expected = 5,
                    actual = dataHolder.receiptList.size,
                )
                val updatedCategory = addedReceipt.category!!
                assertTrue("追加されたreceiptのfeeCategoryの最終利用時刻が更新されていません") {
                    updatedCategory.lastUsedAt > targetCategoryLastUsedAtTime
                }
            }
        }

    @Test
    fun testUpdate() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)
            val targetReceiptId = dataHolder.receiptList.first().id
            val targetCategory = dataHolder.feeCategoryList.first().copy()
            val targetCategoryLastUsedAtTime = targetCategory.lastUsedAt

            // when
            val result =
                repository.update(
                    receiptId = targetReceiptId,
                    cost = 9999,
                    category = targetCategory,
                    createdAt = Clock.System.now(),
                )

            // then
            checkResult(result, "update()が失敗しています。") { updatedReceipt ->
                val targetReceiptFromDataHolder = dataHolder.receiptList.first { it.id == targetReceiptId }
                assertTrue("costが期待通り更新されていないようです") {
                    targetReceiptFromDataHolder.cost == 9999
                }
                val updatedCategory = updatedReceipt.category!!
                assertTrue("更新されたreceiptのfeeCategoryの最終利用時刻が更新されていません") {
                    updatedCategory.lastUsedAt > targetCategoryLastUsedAtTime
                }
            }
        }

    @Test
    fun testDelete() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = makeDataHolder()
            val repository = makeRepository(dataHolder)
            val targetRoomReceipt = dataHolder.receiptList.first()
            val targetCategory = dataHolder.feeCategoryList.find { it.id == targetRoomReceipt.categoryId }
            val targetReceipt = targetRoomReceipt.toDomainModel(targetCategory)

            // when
            val result = repository.delete(targetReceipt)

            // then
            checkResult(result, "delete()が失敗しています。") {
                assertEquals(
                    "削除したのでデータ件数が1件減っているはずです",
                    expected = 3,
                    actual = dataHolder.receiptList.size,
                )
                assertFalse("削除したアイテムがdataHolderから消えていません") {
                    dataHolder.receiptList.any { it.id == targetReceipt.id }
                }
            }
        }

    /**
     * 11/1, 10/1, 9/01, 08/01が作成日のReceiptを持つ
     * SimulatedDataHolderを作成する
     */
    @OptIn(ExperimentalUuidApi::class)
    private fun makeDataHolder(): SimulatedDataHolder {
        val year = 2024
        val baseMonth = 11
        val feeCategories =
            FeeCategory.INITIAL_CATEGORIES.map { categoryName ->
                val id = Uuid.random().toHexString()
                // カテゴリの作成日は適当な値を入れる
                val yearMonth = YearMonth(year, baseMonth)
                FeeCategory(
                    id,
                    categoryName,
                    yearMonth.toLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
                )
            }
        val roomReceipts =
            feeCategories.mapIndexed { index, feeCategory ->
                val id = Uuid.random().toHexString()
                val cost = 1000
                val yearMonth = YearMonth(year, baseMonth - index)
                val yearMonthLocalDateTime = yearMonth.toLocalDateTime()
                // 年月だけでなく月日の値に関わらず期待値通りの結果が得られることを確認するための設定
                val createdAt =
                    LocalDateTime(
                        year = yearMonthLocalDateTime.year,
                        monthNumber = yearMonthLocalDateTime.monthNumber,
                        dayOfMonth = 29,
                        hour = 23,
                        minute = 59,
                    )
                RoomReceipt(
                    id,
                    cost,
                    feeCategory.id,
                    createdAt.toInstant(TimeZone.currentSystemDefault()),
                )
            }
        return SimulatedDataHolder(
            feeCategories,
            roomReceipts,
        )
    }

    private fun makeRepository(dataHolder: SimulatedDataHolder): ReceiptRepository {
        val feeCategoryDao = SimulatedFeeCategoryDao(dataHolder)
        val receiptDao = SimulatedRoomReceiptDao(dataHolder)
        val feeCategoryDataSource = RoomFeeCategoryDataSource(feeCategoryDao)
        val receiptDataSource = RoomReceiptDataSource(receiptDao, feeCategoryDataSource)
        return ReceiptRepositoryImpl(receiptDataSource)
    }

    private fun <T : Any> checkResult(
        result: KmpResult<T>,
        failedMsg: String,
        onSuccess: (T) -> Unit,
    ) {
        when (result) {
            is KmpResult.Failure -> {
                fail("$failedMsg error =  ${result.error}")
            }
            is KmpResult.Success -> {
                onSuccess(result.value)
            }
        }
    }
}
