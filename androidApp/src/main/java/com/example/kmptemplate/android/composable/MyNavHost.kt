package com.example.kmptemplate.android.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kmptemplate.android.MainViewModel

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val initialLoadingState by viewModel.initialLoadingState.collectAsStateWithLifecycle()
    val headerState by viewModel.headerState.collectAsStateWithLifecycle()
    val receiptCollection by viewModel.receiptCollection.collectAsStateWithLifecycle()
    val startYearMonth by viewModel.startYearMonth.collectAsStateWithLifecycle()
    val endYearMonth by viewModel.endYearMonth.collectAsStateWithLifecycle()
    val selectedReceipt by viewModel.selectedReceipt.collectAsStateWithLifecycle()
    val feeCategoryList by viewModel.feeCategoryList.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = Route.Top.name,
        modifier = modifier
    ) {
        composable(Route.Top.name) {
            TopScreen(
                receiptCollection = receiptCollection,
                headerState = headerState,
                loadingState = initialLoadingState,
                startYearMonth = startYearMonth,
                endYearMonth = endYearMonth,
                interactions = viewModel,
                navigateToReceiptDetail =  { navController.navigate(Route.ReceiptDetail.name) }
            )
        }
        composable(Route.ReceiptDetail.name) {
            ReceiptDetailScreen(
                targetReceipt = selectedReceipt,
                feeCategoryList = feeCategoryList,
                headerState = headerState,
                onEditReceipt = { viewModel.onEditReceipt(it) },
                onBack = { navController.popBackStack() },
                onDelete = {
                    viewModel.onDeleteReceipt(it)
                    navController.popBackStack()
                },
            )
        }
    }
}

sealed interface Route {
    data object Top : Route
    data object ReceiptDetail :Route

    val name: String
        get() {
            return when (this) {
                ReceiptDetail -> "ReceiptDetail"
                Top -> "Top"
            }
        }
}
