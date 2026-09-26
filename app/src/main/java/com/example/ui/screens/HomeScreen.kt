package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.SymbolCategory
import com.example.data.model.TradingSymbol
import com.example.ui.components.CyborgHeroBanner
import com.example.ui.components.SymbolGrid
import com.example.ui.components.TradezillerHeader
import com.example.ui.components.UploadChartCard
import com.example.ui.theme.ObsidianBlack

@Composable
fun HomeScreen(
    selectedCategory: SymbolCategory,
    selectedSymbol: TradingSymbol,
    symbols: List<TradingSymbol>,
    isGridExpanded: Boolean,
    onCategorySelected: (SymbolCategory) -> Unit,
    onSymbolSelected: (TradingSymbol) -> Unit,
    onToggleGridExpanded: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    onSampleSelected: (Int) -> Unit,
    onHistoryClick: () -> Unit,
    onCalculatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("home_screen")
    ) {
        // App Header
        TradezillerHeader(
            onHistoryClick = onHistoryClick,
            onCalculatorClick = onCalculatorClick
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // 1. Cyborg Hero Banner
            CyborgHeroBanner()

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Symbol Chooser Grid
            SymbolGrid(
                selectedCategory = selectedCategory,
                selectedSymbol = selectedSymbol,
                symbols = symbols,
                isExpanded = isGridExpanded,
                onCategorySelected = onCategorySelected,
                onSymbolSelected = onSymbolSelected,
                onToggleExpanded = onToggleGridExpanded
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 3. Upload a Chart to Scan Card
            UploadChartCard(
                onImageSelected = onImageSelected,
                onSampleSelected = onSampleSelected
            )
        }
    }
}
