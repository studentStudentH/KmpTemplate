package com.example.kmptemplate.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplate.android.uiState.HeaderState
import com.example.kmptemplate.android.uiState.LoadingState
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.ReceiptCollection
import com.example.kmptemplate.domainmodel.YearMonth
import com.example.kmptemplate.repository.FeeCategoryRepository
import com.example.kmptemplate.repository.ReceiptRepository
import com.example.kmptemplate.util.KermitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val receiptRepository: ReceiptRepository,
    private val feeCategoryRepository: FeeCategoryRepository,
) : ViewModel(), TopScreenInteractions {
    private val _headerState = MutableStateFlow<HeaderState>(HeaderState.None)
    val headerState: StateFlow<HeaderState> = _headerState

    private val _receiptCollection = MutableStateFlow(ReceiptCollection(listOf()))
    val receiptCollection: StateFlow<ReceiptCollection> = _receiptCollection

    private val _initialLoadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val initialLoadingState: StateFlow<LoadingState> = _initialLoadingState

    // 表示対象のreceiptの開始月
    private val _startYearMonth =
        MutableStateFlow(
            YearMonth.makeCurrentYearMonth(),
        )
    val startYearMonth: StateFlow<YearMonth> = _startYearMonth

    private val _endYearMonth = MutableStateFlow<YearMonth?>(null)
    val endYearMonth: StateFlow<YearMonth?> = _endYearMonth

    init {
        KermitLogger.d(TAG) { "init" }
        loadInitialReceipts()
    }

    // 初めてデータを取得する時に呼び出される
    private fun loadInitialReceipts() {
        _initialLoadingState.value = LoadingState.Loading
        viewModelScope.launch {
            val result = loadReceipts()
            KermitLogger.d(TAG) { "loadInitialReceiptResult = $result" }
            when (result) {
                is KmpResult.Failure -> {
                    _initialLoadingState.value = LoadingState.LoadFailed(result.error.msg)
                    // 初めての検索時は検索条件が違うかもしれないから消す必要がある
                    _receiptCollection.value = ReceiptCollection(listOf())
                }
                is KmpResult.Success -> {
                    _initialLoadingState.value = LoadingState.Completed
                    _receiptCollection.value = result.value
                }
            }
        }
    }

    // receipt詳細画面などから戻った時や、検索条件が変わった時に呼び出される
    fun reloadReceipts() {
        _headerState.value = HeaderState.Normal(HEADER_RELOAD_MSG)
        viewModelScope.launch {
            val result = loadReceipts()
            KermitLogger.d(TAG) { "reloadReceiptResult = $result" }
            when (result) {
                is KmpResult.Failure -> {
                    _headerState.value = HeaderState.Error(HEADER_RELOAD_FAILED_MSG)
                }
                is KmpResult.Success -> {
                    _headerState.value = HeaderState.None
                    _receiptCollection.value = result.value
                }
            }
        }
    }

    // 検索対象の指定の仕方に合わせてクエリメソッドを実行
    private suspend fun loadReceipts(): KmpResult<ReceiptCollection> {
        val endYearMonthValue =
            endYearMonth.value
                ?: return receiptRepository.getReceiptsNewerThan(
                    startYearMonth.value.year,
                    startYearMonth.value.month,
                )
        return receiptRepository.getReceiptsBetween(
            startYearMonth.value.year,
            startYearMonth.value.month,
            endYearMonthValue.year,
            endYearMonthValue.month,
        )
    }

    // try catchを後で消す
    override fun onStartYearMonthChanged(newStartYearMonth: YearMonth) {
        try {
            _startYearMonth.value = newStartYearMonth
            reloadReceipts()
        } catch (e: IllegalArgumentException) {
            KermitLogger.e(TAG) { "onStartYearMonthChanged() e = $e" }
            _headerState.value = HeaderState.Error("入力が不正です")
        }
    }

    // try catchを後で消す
    override fun onEndYearMonthChanged(newEndYearMonth: YearMonth) {
        try {
            _endYearMonth.value = newEndYearMonth
            reloadReceipts()
        } catch (e: IllegalArgumentException) {
            KermitLogger.e(TAG) { "onEndYearMonthChanged() e = $e" }
            _headerState.value = HeaderState.Error("入力が不正です")
        }
    }

    fun chengeToPrevMonth() {
        val nextEndMonth = _startYearMonth.value
        val nextStartMonth = nextEndMonth.makePrevMonth()
        _startYearMonth.value = nextStartMonth
        _endYearMonth.value = nextEndMonth
        reloadReceipts()
    }

    fun changeToNextMonth() {
        val nextStartMonth = _startYearMonth.value
        val nextEndMonth =
            if (_endYearMonth.value == YearMonth.makeCurrentYearMonth()) {
                null
            } else {
                _endYearMonth.value?.makeNextMonth()
            }
        _startYearMonth.value = nextStartMonth
        _endYearMonth.value = nextEndMonth
        reloadReceipts()
    }

    override fun onReceiptSelected(receipt: Receipt) {
        KermitLogger.d(TAG) { "onReceiptSelected() receipt = $receipt" }
        // ToDo()
    }

    // Addを押すと数字入力画面が現れて数字を入れるとこの関数が呼び出される
    override fun onAddReceipt(cost: Int) {
        KermitLogger.d(TAG) { "onAddReceipt() cost = $cost" }
        // TODO()
    }

    private companion object {
        const val TAG = "MainViewModel"
        const val HEADER_RELOAD_MSG = "reloading..."
        const val HEADER_RELOAD_FAILED_MSG = "reload failed"
    }
}

interface TopScreenInteractions {
    fun onReceiptSelected(receipt: Receipt)

    fun onAddReceipt(cost: Int)

    fun onStartYearMonthChanged(newStartYearMonth: YearMonth)

    fun onEndYearMonthChanged(newEndYearMonth: YearMonth)
}
