package com.example.data.model

enum class SignalDirection {
    BUY,
    SELL
}

data class TradeSignal(
    val id: Long = 0,
    val symbol: String,
    val direction: SignalDirection,
    val entryPrice: String,
    val stopLoss: String,
    val takeProfit: String,
    val strategy: String = "SMC Liquidity Sweep",
    val timeframe: String = "M30",
    val confidence: Int = 75,
    val summaryTitle: String,
    val detailedAnalysis: String,
    val imageUri: String? = null,
    val sampleImageRes: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val executedTradesCount: Int = 7,
    val riskReward: String = "1:2.8"
)
