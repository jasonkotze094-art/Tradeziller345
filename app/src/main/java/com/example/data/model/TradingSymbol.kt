package com.example.data.model

enum class SymbolCategory(val displayName: String) {
    ALL("All"),
    METALS("Metals"),
    INDICES("Indices"),
    FOREX("Forex"),
    CRYPTO("Crypto"),
    ENERGY("Energy")
}

data class TradingSymbol(
    val code: String,
    val name: String,
    val category: SymbolCategory,
    val defaultDecimals: Int = 2,
    val pipValue: Double = 10.0
)

object TradingPairsData {
    val allPairs: List<TradingSymbol> = listOf(
        // Metals
        TradingSymbol("XAUUSD", "Gold / USD", SymbolCategory.METALS, defaultDecimals = 2),
        TradingSymbol("XAUUSD.M", "Gold Micro", SymbolCategory.METALS, defaultDecimals = 2),
        TradingSymbol("XAUUSDM", "Gold Mini", SymbolCategory.METALS, defaultDecimals = 2),
        TradingSymbol("XAGUSD", "Silver / USD", SymbolCategory.METALS, defaultDecimals = 3),
        TradingSymbol("XPTUSD", "Platinum / USD", SymbolCategory.METALS, defaultDecimals = 2),
        TradingSymbol("XPDUSD", "Palladium / USD", SymbolCategory.METALS, defaultDecimals = 2),

        // Indices
        TradingSymbol("US30.STD", "Dow Jones 30", SymbolCategory.INDICES, defaultDecimals = 1),
        TradingSymbol("US100.STD", "Nasdaq 100", SymbolCategory.INDICES, defaultDecimals = 2),
        TradingSymbol("US500.STD", "S&P 500", SymbolCategory.INDICES, defaultDecimals = 2),
        TradingSymbol("GER40", "DAX 40 Germany", SymbolCategory.INDICES, defaultDecimals = 1),
        TradingSymbol("UK100", "FTSE 100 UK", SymbolCategory.INDICES, defaultDecimals = 1),
        TradingSymbol("JP225", "Nikkei 225 Japan", SymbolCategory.INDICES, defaultDecimals = 0),
        TradingSymbol("FRA40", "CAC 40 France", SymbolCategory.INDICES, defaultDecimals = 1),
        TradingSymbol("AUS200", "ASX 200 Australia", SymbolCategory.INDICES, defaultDecimals = 1),
        TradingSymbol("HK50", "Hang Seng 50", SymbolCategory.INDICES, defaultDecimals = 0),

        // Forex
        TradingSymbol("EURUSD", "Euro / US Dollar", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("GBPUSD", "British Pound / USD", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("USDJPY", "US Dollar / Japanese Yen", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("USDCHF", "US Dollar / Swiss Franc", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("AUDUSD", "Australian Dollar / USD", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("USDCAD", "US Dollar / Canadian Dollar", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("NZDUSD", "New Zealand Dollar / USD", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("EURGBP", "Euro / British Pound", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("EURJPY", "Euro / Japanese Yen", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("GBPJPY", "British Pound / Japanese Yen", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("AUDJPY", "Australian Dollar / JPY", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("CHFJPY", "Swiss Franc / JPY", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("NZDJPY", "NZ Dollar / Japanese Yen", SymbolCategory.FOREX, defaultDecimals = 3),
        TradingSymbol("EURAUD", "Euro / Australian Dollar", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("GBPAUD", "British Pound / AUD", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("EURCHF", "Euro / Swiss Franc", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("GBPCHF", "British Pound / CHF", SymbolCategory.FOREX, defaultDecimals = 5),
        TradingSymbol("AUDCAD", "Australian Dollar / CAD", SymbolCategory.FOREX, defaultDecimals = 5),

        // Crypto
        TradingSymbol("BTCUSD.M", "Bitcoin Micro", SymbolCategory.CRYPTO, defaultDecimals = 2),
        TradingSymbol("ETHUSD", "Ethereum / USD", SymbolCategory.CRYPTO, defaultDecimals = 2),
        TradingSymbol("SOLUSD", "Solana / USD", SymbolCategory.CRYPTO, defaultDecimals = 2),
        TradingSymbol("BNBUSD", "Binance Coin / USD", SymbolCategory.CRYPTO, defaultDecimals = 2),
        TradingSymbol("XRPUSD", "Ripple / USD", SymbolCategory.CRYPTO, defaultDecimals = 4),
        TradingSymbol("ADAUSD", "Cardano / USD", SymbolCategory.CRYPTO, defaultDecimals = 4),
        TradingSymbol("DOGEUSD", "Dogecoin / USD", SymbolCategory.CRYPTO, defaultDecimals = 4),
        TradingSymbol("LTCUSD", "Litecoin / USD", SymbolCategory.CRYPTO, defaultDecimals = 2),

        // Energy
        TradingSymbol("USOIL", "WTI Crude Oil", SymbolCategory.ENERGY, defaultDecimals = 2),
        TradingSymbol("UKOIL", "Brent Crude Oil", SymbolCategory.ENERGY, defaultDecimals = 2),
        TradingSymbol("NGAS", "Natural Gas", SymbolCategory.ENERGY, defaultDecimals = 3)
    )
}
