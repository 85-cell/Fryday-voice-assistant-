package com.example.friday.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.friday.model.ChatMessage
import com.example.friday.model.Speaker
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgCardHover
import com.example.friday.theme.BgDark
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkAmber
import com.example.friday.theme.StarkGold
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted
import com.example.friday.theme.TextSubtle

@Composable
fun ConversationalStream(
    messages: List<ChatMessage>,
    onAuthorizeClick: () -> Unit,
    onSpeakReply: (String) -> Unit,
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
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "◈",
                    color = NeonCyan,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEURAL VOICE COMMS // LIVE STREAM",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "${messages.size} TRANSMISSIONS",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = TextSubtle
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Message Items (Show last 5 recent messages)
        val displayMessages = messages.takeLast(6)
        if (displayMessages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Awaiting voice transmission... Speak to Friday",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                displayMessages.forEach { msg ->
                    ChatBubble(
                        message = msg,
                        onAuthorizeClick = onAuthorizeClick,
                        onSpeakReply = onSpeakReply
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onAuthorizeClick: () -> Unit,
    onSpeakReply: (String) -> Unit
) {
    val isFriday = message.speaker == Speaker.FRIDAY

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isFriday) {
                    Brush.horizontalGradient(listOf(BgCard, BgCardHover))
                } else {
                    Brush.horizontalGradient(listOf(BgDark, BgCard))
                }
            )
            .border(
                1.dp,
                if (isFriday) CyanBorderActive.copy(alpha = 0.5f) else StarkGold.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        // Speaker Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isFriday) NeonCyan else StarkGold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isFriday) "F.R.I.D.A.Y." else "BOSS (VOICE)",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = if (isFriday) NeonCyan else StarkGold
                )
            }

            Text(
                text = message.timestamp,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Message Body
        Text(
            text = message.text,
            fontFamily = if (isFriday) FontFamily.SansSerif else FontFamily.Monospace,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = if (isFriday) TextCyan else StarkGold.copy(alpha = 0.95f)
        )

        // Special interactive actions within bubble
        if (message.systemActionType == "PERMISSION_PROMPT" || message.systemActionType == "PERMISSION_REQUEST") {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAuthorizeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StarkGold,
                    contentColor = BgDark
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("bubble_authorize_button")
            ) {
                Text(
                    text = "AUTHORIZE ALL ACCESS NOW",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}
