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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgCardHover
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
fun CommandConsole(
    isListening: Boolean,
    transcript: String,
    onToggleListening: () -> Unit,
    onSubmitCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var typedText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgDarkSurface)
            .border(1.dp, if (isListening) CyanBorderActive else CyanBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // Transcript or Status Display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BgDark)
                .border(1.dp, if (isListening) NeonCyan.copy(alpha = 0.6f) else CyanBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isListening) StarkGold else NeonCyan)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = transcript,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = if (isListening) StarkGold else TextCyan,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input & Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Text input field
            OutlinedTextField(
                value = typedText,
                onValueChange = { typedText = it },
                placeholder = {
                    Text(
                        text = "Or type Stark command...",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (typedText.isNotBlank()) {
                            onSubmitCommand(typedText)
                            typedText = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyanBorder,
                    focusedTextColor = TextCyan,
                    unfocusedTextColor = TextCyan,
                    focusedContainerColor = BgCard,
                    unfocusedContainerColor = BgCard
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("command_input_field")
            )

            // Submit Button if typing
            if (typedText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonCyan)
                        .clickable {
                            onSubmitCommand(typedText)
                            typedText = ""
                        }
                        .testTag("send_command_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "➤",
                        fontSize = 16.sp,
                        color = BgDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Big Voice Microphone Arc Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isListening) {
                            Brush.verticalGradient(listOf(StarkGold, Color(0xFFCC8800)))
                        } else {
                            Brush.verticalGradient(listOf(BgCardHover, BgCard))
                        }
                    )
                    .border(
                        1.5.dp,
                        if (isListening) StarkGold else NeonCyan,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(onClick = onToggleListening)
                    .testTag("voice_mic_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isListening) "⏹" else "🎙",
                    fontSize = 18.sp,
                    color = if (isListening) BgDark else NeonCyan
                )
            }
        }
    }
}
