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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ReceiptRepositoryTest {
    @Test
    fun testGetAllReceipts() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // given
        val dataHolder = makeDataHolder()
        val repository = makeRepository(dataHolder)

        // when
        val result = repository.getAllReceipts()

        // then
        checkResult(result, "getAllReceipts()が失敗しています。") { receiptCollection ->
            val receipts = receiptCollection.sortByInstantDecending()
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
    fun testGetReceiptsNewerThan() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // given
        val dataHolder = makeDataHolder()
        val repository = makeRepository(dataHolder)

        // when
        val result = repository.getReceiptsNewerThan(2024, 10)

        // then
        checkResult(result, "getReceiptsNewerThan()が失敗しています。") { receiptCollection ->
            val receipts = receiptCollection.sortByInstantDecending()
            assertEquals(
                "Receiptの件数が期待と違います",
                expected = 2,
                actual = receipts.size
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
    fun testGetReceiptsBetween() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // given
        val dataHolder = makeDataHolder()
        val repository = makeRepository(dataHolder)

        // when
        val result = repository.getReceiptsBetween(
            oldestYear = 2024,
            oldestMonth = 9,
            newestYear = 2024,
            newestMonth = 10,
        )

        // then
        checkResult(result, "getReceiptsNewerThan()が失敗しています。") { receiptCollection ->
            val receipts = receiptCollection.sortByInstantDecending()
            assertEquals(
                "Receiptの件数が期待と違います",
                expected = 2,
                actual = receipts.size
            )
            val prevYearMonth = receiptCollection.getPrevYearMonth()
            assertEquals(
                "取得したReceiptの前の月は8月のはずです",
                expected = 8,
                actual = prevYearMonth.month
            )
            val nextYearMonth = receiptCollection.getNextYearMonth()
            assertEquals(
                "取得したReceiptの次の月は11月のはずです",
                expected = 11,
                actual = nextYearMonth.month
            )
        }
    }

    @Test
    fun testAdd() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // ToDo
    }

    @Test
    fun testUpdate() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // ToDo
    }

    @Test
    fun testDelete() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // ToDo
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
                    yearMonth.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
                )
            }
        val roomReceipts =
            feeCategories.mapIndexed { index, feeCategory ->
                val id = Uuid.random().toHexString()
                val cost = 1000
                val yearMonth = YearMonth(year, baseMonth - index)
                RoomReceipt(
                    id,
                    cost,
                    feeCategory.id,
                    yearMonth.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
                )
            }
        return SimulatedDataHolder(
            feeCategories,
            roomReceipts,
        )
    }

    private fun makeRepository(
        dataHolder: SimulatedDataHolder,
    ): ReceiptRepository {
        val feeCategoryDao = SimulatedFeeCategoryDao(dataHolder)
        val receiptDao = SimulatedRoomReceiptDao(dataHolder)
        val feeCategoryDataSource = RoomFeeCategoryDataSource(feeCategoryDao)
        val receiptDataSource = RoomReceiptDataSource(receiptDao, feeCategoryDataSource)
        return ReceiptRepositoryImpl(receiptDataSource)
    }

    private fun <T: Any> checkResult(
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
