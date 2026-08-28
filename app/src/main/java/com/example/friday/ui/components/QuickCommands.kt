package com.example.friday.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgCardHover
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkGold
import com.example.friday.theme.StatusGreen
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

data class StarkQuickCmd(
    val id: String,
    val icon: String,
    val title: String,
    val subtitle: String,
    val commandPhrase: String,
    val isActive: Boolean = false
)

@Composable
fun QuickCommands(
    isTorchOn: Boolean,
    batteryLevel: Int,
    onExecuteCommand: (String) -> Unit,
    onToggleTorch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val commands = listOf(
        StarkQuickCmd(
            id = "torch",
            icon = if (isTorchOn) "⚡" else "💡",
            title = if (isTorchOn) "TORCH ON" else "ARC TORCH",
            subtitle = if (isTorchOn) "Optical active" else "Toggle light",
            commandPhrase = if (isTorchOn) "Turn off flashlight" else "Turn on flashlight",
            isActive = isTorchOn
        ),
        StarkQuickCmd(
            id = "battery",
            icon = "🔋",
            title = "BATTERY $batteryLevel%",
            subtitle = "Power telemetry",
            commandPhrase = "Check battery level"
        ),
        StarkQuickCmd(
            id = "contacts",
            icon = "◎",
            title = "CONTACTS",
            subtitle = "Directory query",
            commandPhrase = "Show my contacts"
        ),
        StarkQuickCmd(
            id = "camera",
            icon = "⌖",
            title = "OPTICAL CAM",
            subtitle = "Launch camera",
            commandPhrase = "Open camera"
        ),
        StarkQuickCmd(
            id = "storage",
            icon = "▣",
            title = "STORAGE SCAN",
            subtitle = "Memory matrix",
            commandPhrase = "Scan files and storage"
        ),
        StarkQuickCmd(
            id = "status",
            icon = "◈",
            title = "STATUS REPORT",
            subtitle = "Mark 85 health",
            commandPhrase = "Friday status report"
        )
    )

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
                    text = "⚡",
                    color = StarkGold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AUTONOMOUS STARK HUD COMMANDS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "1-TAP // VOICE READY",
                fontFamily = FontFamily.Monospace,
                fontSize = 8.sp,
                color = StarkGold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2x3 Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commands.take(3).forEach { cmd ->
                    QuickCommandCard(
                        cmd = cmd,
                        onClick = {
                            if (cmd.id == "torch") onToggleTorch()
                            else onExecuteCommand(cmd.commandPhrase)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commands.drop(3).take(3).forEach { cmd ->
                    QuickCommandCard(
                        cmd = cmd,
                        onClick = { onExecuteCommand(cmd.commandPhrase) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickCommandCard(
    cmd: StarkQuickCmd,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (cmd.isActive) {
                    Brush.verticalGradient(listOf(BgCardHover, BgCard))
                } else {
                    Brush.verticalGradient(listOf(BgCard, BgDarkSurface))
                }
            )
            .border(
                1.dp,
                if (cmd.isActive) StatusGreen else CyanBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
            .testTag("quick_cmd_${cmd.id}")
    ) {
        Text(
            text = cmd.icon,
            fontSize = 16.sp,
            color = if (cmd.isActive) StatusGreen else NeonCyan
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cmd.title,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = if (cmd.isActive) StatusGreen else TextCyan,
            maxLines = 1
        )
        Text(
            text = cmd.subtitle,
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}
