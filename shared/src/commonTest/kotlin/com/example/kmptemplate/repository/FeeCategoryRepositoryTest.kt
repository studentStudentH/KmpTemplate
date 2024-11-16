package com.example.kmptemplate.repository

import com.example.kmptemplate.database.SimulatedDataHolder
import com.example.kmptemplate.database.SimulatedFeeCategoryDao
import com.example.kmptemplate.datasource.RoomFeeCategoryDataSource
import com.example.kmptemplate.domainmodel.KmpResult
import kotlinx.coroutines.test.runTest
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class FeeCategoryRepositoryTest {
    @Test
    fun testGetAll() = runTest(
        timeout = 5.0.toDuration(DurationUnit.SECONDS)
    ) {
        // given
        val dataHolder = SimulatedDataHolder.makeTestData()
        val dao = SimulatedFeeCategoryDao(dataHolder)
        val datasource = RoomFeeCategoryDataSource(dao)
        val repository = FeeCategoryRepositoryImpl(datasource)

        // when
        val result = repository.getAllCategory()

        // then
        dataHolder.feeCategoryList.forEach {
            println("actual = $it")
        }
        when (result) {
            is KmpResult.Failure -> {
                fail("getAll()が失敗しています。error =  ${result.error}")
            }
            is KmpResult.Success -> {
                val actualList = result.value.getMostRecentlyUsedList()
                actualList.forEach {
                    println("actual = $it")
                    assertTrue("dataHolderに存在しないデータが出力されました") {
                        it in dataHolder.feeCategoryList
                    }
                }
                assertEquals(
                    "出力されたアイテム数がdataHolderと一致しません",
                    dataHolder.feeCategoryList.size,
                    actualList.size
                )
            }
        }
    }
}
