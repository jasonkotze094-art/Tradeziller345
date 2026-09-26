package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TradezillerOrange

@Composable
fun CyborgHeroBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag("hero_cyborg_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
        border = BorderStroke(1.dp, DarkCardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(ObsidianBlack),
            contentAlignment = Alignment.Center
        ) {
            // Hero Image
            Image(
                painter = painterResource(id = R.drawable.hero_trader),
                contentDescription = "Tradeziller Cyborg Trader",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

            // Dark vignette overlay with subtle flame-orange ambient glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x66090C10),
                                Color(0xDD090C10)
                            )
                        )
                    )
            )

            // Inner border accent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .border(
                        BorderStroke(1.dp, Brush.verticalGradient(listOf(TradezillerOrange.copy(alpha = 0.4f), Color.Transparent))),
                        shape = RoundedCornerShape(20.dp)
                    )
            )
        }
    }
}
