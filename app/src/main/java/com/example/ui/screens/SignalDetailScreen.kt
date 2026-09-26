package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.SymbolCategory
import com.example.data.model.TradeSignal
import com.example.data.model.TradingSymbol
import com.example.ui.components.LotSizeCalculatorDialog
import com.example.ui.components.SignalHeaderCard
import com.example.ui.components.SymbolGrid
import com.example.ui.components.TradeCountSelector
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradezillerOrange

@Composable
fun SignalDetailScreen(
    signal: TradeSignal,
    selectedCategory: SymbolCategory,
    selectedSymbol: TradingSymbol,
    symbols: List<TradingSymbol>,
    isGridExpanded: Boolean,
    onCategorySelected: (SymbolCategory) -> Unit,
    onSymbolSelected: (TradingSymbol) -> Unit,
    onToggleGridExpanded: () -> Unit,
    onTradeCountChanged: (Int) -> Unit,
    onScanNewChart: (Uri) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCalculator by remember { mutableStateOf(false) }

    // Intercept hardware / system back navigation
    BackHandler { onBack() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onScanNewChart(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("signal_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Text(
                text = "${signal.symbol} Signal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Row {
                IconButton(
                    onClick = { showCalculator = true },
                    modifier = Modifier.testTag("calc_action_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Risk Calculator",
                        tint = TradezillerOrange
                    )
                }
                IconButton(
                    onClick = {
                        val text = "TRADEZILLER SIGNAL: ${signal.symbol} ${signal.direction}\nEntry: ${signal.entryPrice}\nSL: ${signal.stopLoss}\nTP: ${signal.takeProfit}\nStrategy: ${signal.strategy} (${signal.timeframe})"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Tradeziller Signal", text))
                        Toast.makeText(context, "Signal copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("copy_signal_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Signal",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // 1. Signal Header Card (Symbol, SELL/BUY, ENTRY, SL, TP, Strategy, Timeframe, Confidence)
            SignalHeaderCard(signal = signal)

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Chart Preview & Detailed Analysis Narrative
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .testTag("analysis_detail_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Chart Image Preview if available
                    if (signal.imageUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(BorderStroke(1.dp, DarkCardBorder), RoundedCornerShape(14.dp))
                        ) {
                            AsyncImage(
                                model = signal.imageUri,
                                contentDescription = "Scanned Chart",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else if (signal.sampleImageRes != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(BorderStroke(1.dp, DarkCardBorder), RoundedCornerShape(14.dp))
                        ) {
                            Image(
                                painter = painterResource(id = signal.sampleImageRes),
                                contentDescription = "Scanned Chart",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Analysis Title
                    Text(
                        text = signal.summaryTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TradezillerOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Analysis Narrative Body
                    Text(
                        text = signal.detailedAnalysis,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Symbol quick selector
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

            // 4. Trade Count Selector (1, 3, 7, 9)
            TradeCountSelector(
                selectedCount = signal.executedTradesCount,
                onCountSelected = onTradeCountChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Scan Another Chart Button (from video)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("scan_another_chart_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TradezillerOrange)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Scan Another Chart",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showCalculator) {
        LotSizeCalculatorDialog(
            tradeCount = signal.executedTradesCount,
            symbol = signal.symbol,
            onDismiss = { showCalculator = false }
        )
    }
}
