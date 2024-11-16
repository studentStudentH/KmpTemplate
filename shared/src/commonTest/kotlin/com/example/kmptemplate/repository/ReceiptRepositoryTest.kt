package com.example.kmptemplate.repository

import com.example.kmptemplate.database.SimulatedDataHolder
import com.example.kmptemplate.database.SimulatedFeeCategoryDao
import com.example.kmptemplate.database.SimulatedRoomReceiptDao
import com.example.kmptemplate.datasource.RoomFeeCategoryDataSource
import com.example.kmptemplate.datasource.RoomReceiptDataSource
import com.example.kmptemplate.domainmodel.KmpResult
import kotlinx.coroutines.test.runTest
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ReceiptRepositoryTest {
    @Test
    fun testGetAllReceipts() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // given
        val dataHolder = SimulatedDataHolder.makeTestData()
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
        // ToDo
    }

    @Test
    fun testGetReceiptsBetween() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS),
    ) {
        // ToDo
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
