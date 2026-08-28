package com.example.friday.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.model.ActionItem
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgDark
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkGold
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted
import com.example.friday.theme.TextSubtle

@Composable
fun ActivityLog(
    history: List<ActionItem>,
    onUndoClick: (ActionItem) -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgDarkSurface)
            .border(1.dp, CyanBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "▣",
                    color = NeonCyan,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "STARK AUDIT TRAIL // REVERSIBLE ACTIONS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NeonCyan,
                    letterSpacing = 0.8.sp
                )
            }

            if (history.isNotEmpty()) {
                Text(
                    text = "CLEAR ALL",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    color = TextMuted,
                    modifier = Modifier
                        .clickable(onClick = onClearAllClick)
                        .testTag("clear_history_button")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Audit trail clear. Autonomous operations logged here.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextSubtle
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                history.take(5).forEach { item ->
                    ActionLogCard(
                        item = item,
                        onUndoClick = { onUndoClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionLogCard(
    item: ActionItem,
    onUndoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BgCard)
            .border(1.dp, CyanBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.icon,
                fontSize = 14.sp,
                color = NeonCyan
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = item.title,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = TextCyan
                )
                Text(
                    text = "${item.detail} · ${item.time}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }

        if (item.canUndo) {
            Text(
                text = "UNDO",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = StarkGold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(BgDark)
                    .border(1.dp, StarkGold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .clickable(onClick = onUndoClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("undo_${item.id}")
            )
        }
    }
}
