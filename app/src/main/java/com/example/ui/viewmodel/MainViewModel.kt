package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.analyzer.ChartAnalyzer
import com.example.data.local.AppDatabase
import com.example.data.local.SignalEntity
import com.example.data.model.SymbolCategory
import com.example.data.model.TradeSignal
import com.example.data.model.TradingPairsData
import com.example.data.model.TradingSymbol
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AppScreen {
    object Home : AppScreen
    object SignalDetail : AppScreen
    object History : AppScreen
    object Calculator : AppScreen
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val signalDao = AppDatabase.getDatabase(application).signalDao()

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SymbolCategory.ALL)
    val selectedCategory: StateFlow<SymbolCategory> = _selectedCategory.asStateFlow()

    // Default to BTCUSD.M or XAUUSD.M as shown in video
    private val _selectedSymbol = MutableStateFlow(
        TradingPairsData.allPairs.find { it.code == "BTCUSD.M" } ?: TradingPairsData.allPairs.first()
    )
    val selectedSymbol: StateFlow<TradingSymbol> = _selectedSymbol.asStateFlow()

    private val _isGridExpanded = MutableStateFlow(false)
    val isGridExpanded: StateFlow<Boolean> = _isGridExpanded.asStateFlow()

    private val _selectedTradeCount = MutableStateFlow(7)
    val selectedTradeCount: StateFlow<Int> = _selectedTradeCount.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanningStep = MutableStateFlow("Scanning chart...")
    val scanningStep: StateFlow<String> = _scanningStep.asStateFlow()

    private val _scanningSubText = MutableStateFlow("Reading structure & price action")
    val scanningSubText: StateFlow<String> = _scanningSubText.asStateFlow()

    private val _currentSignal = MutableStateFlow<TradeSignal?>(null)
    val currentSignal: StateFlow<TradeSignal?> = _currentSignal.asStateFlow()

    private val _lastScannedImageUri = MutableStateFlow<Uri?>(null)
    val lastScannedImageUri: StateFlow<Uri?> = _lastScannedImageUri.asStateFlow()

    private val _lastSampleResId = MutableStateFlow<Int?>(null)
    val lastSampleResId: StateFlow<Int?> = _lastSampleResId.asStateFlow()

    val savedSignals: StateFlow<List<TradeSignal>> = signalDao.getAllSignals()
        .map { list -> list.map { it.toDomainModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered symbols based on active category
    val displayedSymbols: StateFlow<List<TradingSymbol>> = _selectedCategory.map { category ->
        if (category == SymbolCategory.ALL) {
            TradingPairsData.allPairs
        } else {
            TradingPairsData.allPairs.filter { it.category == category }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TradingPairsData.allPairs)

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectCategory(category: SymbolCategory) {
        _selectedCategory.value = category
    }

    fun selectSymbol(symbol: TradingSymbol) {
        _selectedSymbol.value = symbol
    }

    fun toggleGridExpanded() {
        _isGridExpanded.value = !_isGridExpanded.value
    }

    fun setTradeCount(count: Int) {
        _selectedTradeCount.value = count
        _currentSignal.value?.let { signal ->
            _currentSignal.value = signal.copy(executedTradesCount = count)
        }
    }

    fun scanChart(uri: Uri?, sampleResId: Int? = null) {
        _lastScannedImageUri.value = uri
        _lastSampleResId.value = sampleResId
        _isScanning.value = true
        _scanningStep.value = "Scanning chart..."
        _scanningSubText.value = "Reading structure & price action"

        viewModelScope.launch {
            // Simulated scanner stages to reproduce the realistic scanning UX
            delay(800)
            _scanningStep.value = "Analyzing liquidity..."
            _scanningSubText.value = "Detecting SMC swing highs, BSL & SSL pools"
            delay(900)
            _scanningStep.value = "Locating order blocks..."
            _scanningSubText.value = "Measuring premium vs discount zones & FVGs"
            delay(800)

            val signal = ChartAnalyzer.analyzeChart(
                context = getApplication(),
                imageUri = uri,
                sampleResId = sampleResId,
                selectedSymbolCode = _selectedSymbol.value.code,
                selectedTradeCount = _selectedTradeCount.value
            )

            // Auto-persist in Room
            val entity = SignalEntity.fromDomainModel(signal)
            val newId = signalDao.insertSignal(entity)
            val savedSignal = signal.copy(id = newId)

            _currentSignal.value = savedSignal
            _isScanning.value = false
            _currentScreen.value = AppScreen.SignalDetail
        }
    }

    fun deleteSignal(signal: TradeSignal) {
        viewModelScope.launch {
            signalDao.deleteSignal(SignalEntity.fromDomainModel(signal))
            if (_currentSignal.value?.id == signal.id) {
                _currentSignal.value = null
                _currentScreen.value = AppScreen.Home
            }
        }
    }

    fun loadHistoricalSignal(signal: TradeSignal) {
        _currentSignal.value = signal
        _selectedTradeCount.value = signal.executedTradesCount
        val matchingSymbol = TradingPairsData.allPairs.find { it.code == signal.symbol }
        if (matchingSymbol != null) {
            _selectedSymbol.value = matchingSymbol
        }
        _currentScreen.value = AppScreen.SignalDetail
    }
}
