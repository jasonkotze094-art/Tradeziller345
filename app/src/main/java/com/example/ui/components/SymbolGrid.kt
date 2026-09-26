package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SymbolCategory
import com.example.data.model.TradingSymbol
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradezillerOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymbolGrid(
    selectedCategory: SymbolCategory,
    selectedSymbol: TradingSymbol,
    symbols: List<TradingSymbol>,
    isExpanded: Boolean,
    onCategorySelected: (SymbolCategory) -> Unit,
    onSymbolSelected: (TradingSymbol) -> Unit,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .testTag("symbol_selector_section"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section Header
        Text(
            text = "CHOOSE SYMBOL TO TRADE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Category Pills (Horizontal scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SymbolCategory.values().forEach { category ->
                val isCatSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isCatSelected) TradezillerOrange else DarkSurfaceVariant)
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isCatSelected) TradezillerOrange else DarkCardBorder
                            ),
                            CircleShape
                        )
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("category_pill_${category.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.displayName,
                        fontSize = 12.sp,
                        fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCatSelected) Color.White else TextSecondary
                    )
                }
            }
        }

        // Symbols Grid
        val visibleSymbols = if (isExpanded || symbols.size <= 8) {
            symbols
        } else {
            symbols.take(8)
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            visibleSymbols.forEach { item ->
                val isSelected = item.code == selectedSymbol.code
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) TradezillerOrange else DarkSurface)
                        .border(
                            BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) TradezillerOrange else DarkCardBorder
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSymbolSelected(item) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("symbol_pill_${item.code}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.code,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Color.White else TextPrimary
                    )
                }
            }
        }

        // Expand / Collapse Toggle Button
        if (symbols.size > 8) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onToggleExpanded() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("toggle_pairs_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Show Less" else "Show All ${symbols.size} Pairs",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TradezillerOrange
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TradezillerOrange
                )
            }
        }
    }
}
