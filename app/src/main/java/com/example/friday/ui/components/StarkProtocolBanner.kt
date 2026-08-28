package com.example.friday.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.example.friday.model.PermissionItem
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgDark
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.ElectricBlue
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkAmber
import com.example.friday.theme.StarkCrimson
import com.example.friday.theme.StarkGold
import com.example.friday.theme.StatusGreen
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

@Composable
fun StarkProtocolBanner(
    permissions: List<PermissionItem>,
    allGranted: Boolean,
    onRequestAllPermissions: () -> Unit,
    onOpenPermissionDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val grantedCount = permissions.count { it.isGranted }
    val totalCount = permissions.size

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (allGranted) {
                        listOf(BgDarkSurface, BgCard.copy(alpha = 0.7f))
                    } else {
                        listOf(BgCard, BgDarkSurface)
                    }
                )
            )
            .border(
                1.5.dp,
                if (allGranted) CyanBorder else StarkGold.copy(alpha = 0.7f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Stark Header Tag
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (allGranted) StatusGreen else StarkGold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "STARK PROTOCOL 01: FULL SYSTEM LINK",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (allGranted) NeonCyan else StarkGold,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "[$grantedCount / $totalCount LINKED]",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (allGranted) StatusGreen else StarkGold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Directive text
        Text(
            text = if (allGranted) {
                "F.R.I.D.A.Y. has full autonomous authorization over device subsystems: Contacts, Storage Matrix, Audio Array, and Optical Hardware."
            } else {
                "Primary Directive: F.R.I.D.A.Y. requires your authorization to link device access points (Contacts, Storage, Audio Array, Optical Sensors) for autonomous operations."
            },
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = TextCyan
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of 6 Stark Protocol Nodes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            permissions.take(6).forEach { item ->
                PermissionNodeChip(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!allGranted) {
                Button(
                    onClick = onRequestAllPermissions,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StarkGold,
                        contentColor = BgDark
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("authorize_all_button")
                ) {
                    Text(
                        text = "AUTHORIZE ALL ACCESS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Button(
                onClick = onOpenPermissionDetails,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgCard,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, CyanBorder, RoundedCornerShape(8.dp))
                    .testTag("manage_access_button")
            ) {
                Text(
                    text = if (allGranted) "ACCESS MATRIX" else "DETAILS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun PermissionNodeChip(
    item: PermissionItem,
    modifier: Modifier = Modifier
) {
    val shortLabel = when (item.id) {
        "audio" -> "MIC"
        "contacts" -> "CONTACT"
        "storage" -> "FILES"
        "camera" -> "OPTIC"
        "location" -> "GEO"
        "notifications" -> "NOTIF"
        else -> item.name.take(4).uppercase()
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (item.isGranted) BgDarkSurface else BgDark.copy(alpha = 0.7f))
            .border(
                1.dp,
                if (item.isGranted) CyanBorderActive.copy(alpha = 0.6f) else StarkCrimson.copy(alpha = 0.4f),
                RoundedCornerShape(6.dp)
            )
            .padding(vertical = 6.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.icon,
            fontSize = 12.sp,
            color = if (item.isGranted) NeonCyan else StarkGold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = shortLabel,
            fontFamily = FontFamily.Monospace,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = if (item.isGranted) TextCyan else TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (item.isGranted) StatusGreen else StarkCrimson)
        )
    }
}
