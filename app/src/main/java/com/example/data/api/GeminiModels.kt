package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>,
    @Json(name = "role") val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenConfig(
    @Json(name = "temperature") val temperature: Float? = 0.2f,
    @Json(name = "responseMimeType") val responseMimeType: String? = "application/json"
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class ChartAnalysisResult(
    @Json(name = "symbol") val symbol: String? = null,
    @Json(name = "direction") val direction: String? = null, // BUY or SELL
    @Json(name = "entryPrice") val entryPrice: String? = null,
    @Json(name = "stopLoss") val stopLoss: String? = null,
    @Json(name = "takeProfit") val takeProfit: String? = null,
    @Json(name = "strategy") val strategy: String? = null,
    @Json(name = "timeframe") val timeframe: String? = null,
    @Json(name = "confidence") val confidence: Int? = null,
    @Json(name = "summaryTitle") val summaryTitle: String? = null,
    @Json(name = "detailedAnalysis") val detailedAnalysis: String? = null
)
