package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.components.LotSizeCalculatorDialog
import com.example.ui.components.ScanningOverlay
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SignalDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBlack
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TradezillerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TradezillerApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSymbol by viewModel.selectedSymbol.collectAsState()
    val displayedSymbols by viewModel.displayedSymbols.collectAsState()
    val isGridExpanded by viewModel.isGridExpanded.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanningStep by viewModel.scanningStep.collectAsState()
    val scanningSubText by viewModel.scanningSubText.collectAsState()
    val currentSignal by viewModel.currentSignal.collectAsState()
    val savedSignals by viewModel.savedSignals.collectAsState()

    var showGlobalCalculator by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        containerColor = ObsidianBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                is AppScreen.Home -> {
                    HomeScreen(
                        selectedCategory = selectedCategory,
                        selectedSymbol = selectedSymbol,
                        symbols = displayedSymbols,
                        isGridExpanded = isGridExpanded,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onSymbolSelected = { viewModel.selectSymbol(it) },
                        onToggleGridExpanded = { viewModel.toggleGridExpanded() },
                        onImageSelected = { uri -> viewModel.scanChart(uri) },
                        onSampleSelected = { sampleRes -> viewModel.scanChart(null, sampleRes) },
                        onHistoryClick = { viewModel.navigateTo(AppScreen.History) },
                        onCalculatorClick = { showGlobalCalculator = true }
                    )
                }

                is AppScreen.SignalDetail -> {
                    currentSignal?.let { signal ->
                        SignalDetailScreen(
                            signal = signal,
                            selectedCategory = selectedCategory,
                            selectedSymbol = selectedSymbol,
                            symbols = displayedSymbols,
                            isGridExpanded = isGridExpanded,
                            onCategorySelected = { viewModel.selectCategory(it) },
                            onSymbolSelected = { viewModel.selectSymbol(it) },
                            onToggleGridExpanded = { viewModel.toggleGridExpanded() },
                            onTradeCountChanged = { count -> viewModel.setTradeCount(count) },
                            onScanNewChart = { uri -> viewModel.scanChart(uri) },
                            onBack = { viewModel.navigateTo(AppScreen.Home) }
                        )
                    } ?: run {
                        viewModel.navigateTo(AppScreen.Home)
                    }
                }

                is AppScreen.History -> {
                    HistoryScreen(
                        signals = savedSignals,
                        onSignalClick = { signal -> viewModel.loadHistoricalSignal(signal) },
                        onDeleteSignal = { signal -> viewModel.deleteSignal(signal) },
                        onBack = { viewModel.navigateTo(AppScreen.Home) }
                    )
                }

                is AppScreen.Calculator -> {
                    viewModel.navigateTo(AppScreen.Home)
                }
            }

            // Real-time Scanning HUD Overlay
            if (isScanning) {
                ScanningOverlay(
                    stepText = scanningStep,
                    subText = scanningSubText
                )
            }

            if (showGlobalCalculator) {
                LotSizeCalculatorDialog(
                    tradeCount = viewModel.selectedTradeCount.collectAsState().value,
                    symbol = selectedSymbol.code,
                    onDismiss = { showGlobalCalculator = false }
                )
            }
        }
    }
}
