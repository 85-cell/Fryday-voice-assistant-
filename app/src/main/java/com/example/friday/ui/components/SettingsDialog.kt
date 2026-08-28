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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgDark
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.ElectricBlue
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkGold
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

@Composable
fun SettingsDialog(
    continuousListening: Boolean,
    hapticsEnabled: Boolean,
    speechRate: Float,
    speechPitch: Float,
    onContinuousListeningChange: (Boolean) -> Unit,
    onHapticsChange: (Boolean) -> Unit,
    onSpeechRateChange: (Float) -> Unit,
    onSpeechPitchChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("settings_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BgDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyanBorderActive)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚙", fontSize = 16.sp, color = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "STARK SYSTEM CONFIG",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "✕",
                        color = TextMuted,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable(onClick = onDismiss)
                            .padding(4.dp)
                            .testTag("close_settings_dialog")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Continuous Listening Toggle
                SettingToggleRow(
                    title = "Continuous Voice Link",
                    subtitle = "Auto-listen after Friday finishes speaking",
                    checked = continuousListening,
                    onCheckedChange = onContinuousListeningChange
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Haptic Feedback Toggle
                SettingToggleRow(
                    title = "Stark HUD Haptics",
                    subtitle = "Vibration pulses during command execution",
                    checked = hapticsEnabled,
                    onCheckedChange = onHapticsChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Speech Rate Slider
                Text(
                    text = "FRIDAY SPEECH RATE (${String.format(java.util.Locale.US, "%.2f", speechRate)}x)",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Slider(
                    value = speechRate,
                    onValueChange = onSpeechRateChange,
                    valueRange = 0.8f..1.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = BgCard
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Speech Pitch Slider
                Text(
                    text = "FRIDAY VOCAL PITCH (${String.format(java.util.Locale.US, "%.2f", speechPitch)}x)",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StarkGold
                )
                Slider(
                    value = speechPitch,
                    onValueChange = onSpeechPitchChange,
                    valueRange = 0.8f..1.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = StarkGold,
                        activeTrackColor = StarkGold,
                        inactiveTrackColor = BgCard
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgCard)
            .border(1.dp, CyanBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextCyan
            )
            Text(
                text = subtitle,
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                color = TextMuted
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BgDark,
                checkedTrackColor = NeonCyan,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = BgDark
            )
        )
    }
}
