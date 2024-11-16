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
    fun testGetAll() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
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
                        actualList.size,
                    )
                }
            }
        }

    @Test
    fun testAddCategory() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = SimulatedDataHolder.makeTestData()
            val dao = SimulatedFeeCategoryDao(dataHolder)
            val datasource = RoomFeeCategoryDataSource(dao)
            val repository = FeeCategoryRepositoryImpl(datasource)

            // when
            val result = repository.addCategory("test")

            // then
            when (result) {
                is KmpResult.Failure -> {
                    fail("addCategory()が失敗しています。error =  ${result.error}")
                }
                is KmpResult.Success -> {
                    val actual = result.value
                    assertEquals(
                        "追加したデータがresultで帰ってきていません",
                        "test",
                        actual.name,
                    )
                    assertEquals(
                        "保存されたはずなのにデータ件数が期待値と一致しません",
                        5,
                        dataHolder.feeCategoryList.size,
                    )
                }
            }
        }

    @Test
    fun testRenameCategory() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = SimulatedDataHolder.makeTestData()
            val dao = SimulatedFeeCategoryDao(dataHolder)
            val datasource = RoomFeeCategoryDataSource(dao)
            val repository = FeeCategoryRepositoryImpl(datasource)
            val targetCategory = dataHolder.feeCategoryList.first()

            // when
            val newCategoryName = "rewrote"
            val result = repository.renameCategory(targetCategory.id, newCategoryName)
            val rewroteCategory = dataHolder.feeCategoryList.first { it.id == targetCategory.id }

            // then
            when (result) {
                is KmpResult.Failure -> {
                    fail("renameCategory()が失敗しています。error =  ${result.error}")
                }
                is KmpResult.Success -> {
                    val actual = result.value
                    assertEquals(
                        "適切な編集結果がresultで帰ってきていません",
                        expected = newCategoryName,
                        actual = actual.name,
                    )
                    assertEquals(
                        "上書きされたはずのデータが保存されていません",
                        expected = newCategoryName,
                        actual = rewroteCategory.name,
                    )
                }
            }
        }

    @Test
    fun testDeleteCategory() =
        runTest(
            timeout = 5.0.toDuration(DurationUnit.SECONDS),
        ) {
            // given
            val dataHolder = SimulatedDataHolder.makeTestData()
            val dao = SimulatedFeeCategoryDao(dataHolder)
            val datasource = RoomFeeCategoryDataSource(dao)
            val repository = FeeCategoryRepositoryImpl(datasource)
            val targetCategory = dataHolder.feeCategoryList.first()

            // when
            val result = repository.deleteCategory(targetCategory.id)

            // then
            when (result) {
                is KmpResult.Failure -> {
                    fail("deleteCategory()が失敗しています。error =  ${result.error}")
                }
                is KmpResult.Success -> {
                    assertEquals(
                        "削除したはずなのにデータ件数が減っていません",
                        expected = 3,
                        actual = dataHolder.feeCategoryList.size,
                    )
                    val toBeNull = dataHolder.feeCategoryList.find { it.id == targetCategory.id }
                    if (toBeNull != null) {
                        fail("削除したはずのデータがなぜか見つかります")
                    }
                }
            }
        }
}
