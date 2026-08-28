package com.example.friday.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkGold
import com.example.friday.theme.StatusGreen
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

@Composable
fun HudReadout(
    cpuTemp: String,
    securityStatus: String,
    latencyMs: Int,
    batteryLevel: Int,
    freeStorageGb: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgDarkSurface)
            .border(1.dp, CyanBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TelemetryCell(
            label = "ARC CORE",
            value = "100%",
            valueColor = StatusGreen,
            modifier = Modifier.weight(1f)
        )

        TelemetryDivider()

        TelemetryCell(
            label = "TEMP",
            value = cpuTemp,
            valueColor = TextCyan,
            modifier = Modifier.weight(1f)
        )

        TelemetryDivider()

        TelemetryCell(
            label = "LATENCY",
            value = "${latencyMs}ms",
            valueColor = NeonCyan,
            modifier = Modifier.weight(1f)
        )

        TelemetryDivider()

        TelemetryCell(
            label = "STORAGE",
            value = "${freeStorageGb}G",
            valueColor = StarkGold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TelemetryCell(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 8.sp,
            letterSpacing = 1.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = valueColor
        )
    }
}

@Composable
private fun TelemetryDivider() {
    Box(
        modifier = Modifier
            .height(18.dp)
            .background(CyanBorder)
    )
}
