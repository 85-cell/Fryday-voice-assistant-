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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgDark
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkAmber
import com.example.friday.theme.StarkCrimson
import com.example.friday.theme.StarkGold
import com.example.friday.theme.StatusGreen
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

@Composable
fun TopBar(
    voiceMuted: Boolean,
    allPermissionsGranted: Boolean,
    onToggleMute: () -> Unit,
    onOpenPermissions: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stark Branding
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(BgCard)
                    .border(1.5.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "F",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = NeonCyan
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "F.R.I.D.A.Y.",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = TextCyan,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (allPermissionsGranted) StatusGreen.copy(alpha = 0.2f) else StarkGold.copy(alpha = 0.2f))
                            .border(1.dp, if (allPermissionsGranted) StatusGreen else StarkGold, RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = if (allPermissionsGranted) "ONLINE" else "SETUP REQ",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 7.sp,
                            color = if (allPermissionsGranted) StatusGreen else StarkGold
                        )
                    }
                }

                Text(
                    text = "STARK INDUSTRIES OS // MK-85",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Top Action Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Voice Mute / Unmute
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BgCard)
                    .border(1.dp, if (voiceMuted) StarkCrimson else CyanBorder, RoundedCornerShape(8.dp))
                    .clickable(onClick = onToggleMute)
                    .testTag("mute_toggle_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (voiceMuted) "🔇" else "🔊",
                    fontSize = 14.sp
                )
            }

            // Shield / Permissions
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BgCard)
                    .border(1.dp, if (allPermissionsGranted) StatusGreen else StarkGold, RoundedCornerShape(8.dp))
                    .clickable(onClick = onOpenPermissions)
                    .testTag("shield_permission_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛡",
                    fontSize = 14.sp
                )
            }

            // Settings
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BgCard)
                    .border(1.dp, CyanBorder, RoundedCornerShape(8.dp))
                    .clickable(onClick = onOpenSettings)
                    .testTag("settings_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚙",
                    fontSize = 15.sp,
                    color = NeonCyan
                )
            }
        }
    }
}
