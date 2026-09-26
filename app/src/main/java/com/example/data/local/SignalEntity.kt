package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.SignalDirection
import com.example.data.model.TradeSignal

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val direction: String, // "BUY" or "SELL"
    val entryPrice: String,
    val stopLoss: String,
    val takeProfit: String,
    val strategy: String,
    val timeframe: String,
    val confidence: Int,
    val summaryTitle: String,
    val detailedAnalysis: String,
    val imageUri: String?,
    val timestamp: Long,
    val executedTradesCount: Int,
    val riskReward: String
) {
    fun toDomainModel(sampleRes: Int? = null): TradeSignal {
        return TradeSignal(
            id = id,
            symbol = symbol,
            direction = if (direction == "BUY") SignalDirection.BUY else SignalDirection.SELL,
            entryPrice = entryPrice,
            stopLoss = stopLoss,
            takeProfit = takeProfit,
            strategy = strategy,
            timeframe = timeframe,
            confidence = confidence,
            summaryTitle = summaryTitle,
            detailedAnalysis = detailedAnalysis,
            imageUri = imageUri,
            sampleImageRes = sampleRes,
            timestamp = timestamp,
            executedTradesCount = executedTradesCount,
            riskReward = riskReward
        )
    }

    companion object {
        fun fromDomainModel(model: TradeSignal): SignalEntity {
            return SignalEntity(
                id = model.id,
                symbol = model.symbol,
                direction = model.direction.name,
                entryPrice = model.entryPrice,
                stopLoss = model.stopLoss,
                takeProfit = model.takeProfit,
                strategy = model.strategy,
                timeframe = model.timeframe,
                confidence = model.confidence,
                summaryTitle = model.summaryTitle,
                detailedAnalysis = model.detailedAnalysis,
                imageUri = model.imageUri,
                timestamp = model.timestamp,
                executedTradesCount = model.executedTradesCount,
                riskReward = model.riskReward
            )
        }
    }
}
