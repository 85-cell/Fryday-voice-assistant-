package com.example.friday.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgCore
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.ElectricBlue
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.ReactorCoreWhite
import com.example.friday.theme.StarkAmber
import com.example.friday.theme.StarkGold
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CoreVisualizer(
    isListening: Boolean,
    isSpeaking: Boolean,
    isProcessing: Boolean,
    audioLevel: Float,
    onCoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "arc_reactor_anim")

    // Slow continuous outer ring rotation
    val outerAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_ring_rotation"
    )

    // Reverse fast inner ring rotation
    val innerAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "inner_ring_rotation"
    )

    // Pulse wave when active
    val corePulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isListening || isSpeaking) 600 else 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgDarkSurface, BgCard.copy(alpha = 0.6f))
                )
            )
            .border(1.dp, if (isListening || isSpeaking) CyanBorderActive else CyanBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stark Telemetry Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isListening || isSpeaking) StarkGold else NeonCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "STARK ARC REACTOR // MK-85",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NeonCyan,
                    letterSpacing = 1.5.sp
                )
            }

            Text(
                text = when {
                    isProcessing -> "ANALYZING SPEECH..."
                    isSpeaking -> "F.R.I.D.A.Y. TRANSMITTING"
                    isListening -> "ACOUSTIC SENSORS ACTIVE"
                    else -> "AUTONOMOUS STANDBY"
                },
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = if (isListening || isSpeaking) StarkGold else TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Center Arc Reactor Visualizer
        Box(
            modifier = Modifier
                .size(190.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onCoreClick
                )
                .testTag("arc_reactor_core"),
            contentAlignment = Alignment.Center
        ) {
            // Arc Reactor Custom Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.minDimension / 2f - 8.dp.toPx()

                // Outer Glowing Ring with Ticks
                rotate(outerAngle, pivot = center) {
                    drawCircle(
                        color = CyanBorder.copy(alpha = 0.4f),
                        radius = baseRadius,
                        center = center,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                    )

                    // 8 Outer Angle Markers
                    for (i in 0 until 8) {
                        val angleRad = Math.toRadians((i * 45).toDouble())
                        val x1 = center.x + (baseRadius - 10.dp.toPx()) * cos(angleRad).toFloat()
                        val y1 = center.y + (baseRadius - 10.dp.toPx()) * sin(angleRad).toFloat()
                        val x2 = center.x + baseRadius * cos(angleRad).toFloat()
                        val y2 = center.y + baseRadius * sin(angleRad).toFloat()
                        drawLine(
                            color = NeonCyan.copy(alpha = 0.8f),
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 2.5.dp.toPx()
                        )
                    }
                }

                // Middle Segmented Reactor Ring
                rotate(innerAngle, pivot = center) {
                    val midRadius = baseRadius * 0.76f
                    drawCircle(
                        color = ElectricBlue.copy(alpha = 0.35f),
                        radius = midRadius,
                        center = center,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // 12 Arc Magnet Coils
                    for (i in 0 until 12) {
                        val angleRad = Math.toRadians((i * 30).toDouble())
                        val magnetRadius = midRadius - 6.dp.toPx()
                        val mx = center.x + magnetRadius * cos(angleRad).toFloat()
                        val my = center.y + magnetRadius * sin(angleRad).toFloat()

                        drawCircle(
                            color = if (i % 2 == 0) NeonCyan else StarkGold,
                            radius = 3.5.dp.toPx(),
                            center = Offset(mx, my)
                        )
                    }
                }

                // Audio Reactive Wave Ring
                val dynamicRadius = (baseRadius * 0.52f) * (1f + audioLevel * 0.3f) * corePulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = if (isListening || isSpeaking) 0.6f else 0.25f),
                            ElectricBlue.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = dynamicRadius * 1.3f
                    ),
                    radius = dynamicRadius * 1.2f,
                    center = center
                )

                // High Energy Plasma Core
                val coreRadius = baseRadius * 0.38f * corePulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ReactorCoreWhite,
                            NeonCyan,
                            ElectricBlue.copy(alpha = 0.8f)
                        ),
                        center = center,
                        radius = coreRadius
                    ),
                    radius = coreRadius,
                    center = center
                )

                // Arc Target Reticles
                drawLine(
                    color = NeonCyan.copy(alpha = 0.5f),
                    start = Offset(center.x - coreRadius * 1.3f, center.y),
                    end = Offset(center.x + coreRadius * 1.3f, center.y),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = NeonCyan.copy(alpha = 0.5f),
                    start = Offset(center.x, center.y - coreRadius * 1.3f),
                    end = Offset(center.x, center.y + coreRadius * 1.3f),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Central Micro Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isListening) "LISTENING" else if (isSpeaking) "SPEAKING" else "F.R.I.D.A.Y.",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = if (isListening || isSpeaking) StarkGold else TextCyan
                )
                Text(
                    text = "TAP CORE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Audio Frequency Equalizer Spectrogram Bars
        AudioEqualizerBars(
            audioLevel = audioLevel,
            isActive = isListening || isSpeaking
        )
    }
}

@Composable
private fun AudioEqualizerBars(
    audioLevel: Float,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val barCount = 28
    val transition = rememberInfiniteTransition(label = "eq_bars")

    val pulseFactor by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eq_pulse"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(26.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val distFromCenter = Math.abs(i - barCount / 2f) / (barCount / 2f)
            val centerWeight = 1f - distFromCenter * 0.6f

            val heightFraction = if (isActive) {
                val seed = ((i * 37) % 100) / 100f
                val dynamicHeight = (0.2f + (seed * 0.4f + audioLevel * 0.7f) * centerWeight * pulseFactor).coerceIn(0.15f, 1.0f)
                dynamicHeight
            } else {
                (0.12f + 0.08f * centerWeight)
            }

            val barColor = when {
                !isActive -> CyanBorder
                i % 4 == 0 -> StarkGold
                i % 2 == 0 -> NeonCyan
                else -> ElectricBlue
            }

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((26 * heightFraction).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}
