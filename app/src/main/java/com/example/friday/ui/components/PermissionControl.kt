package com.example.friday.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friday.model.PermissionItem
import com.example.friday.theme.BgCard
import com.example.friday.theme.BgCardHover
import com.example.friday.theme.BgDark
import com.example.friday.theme.BgDarkSurface
import com.example.friday.theme.CyanBorder
import com.example.friday.theme.CyanBorderActive
import com.example.friday.theme.NeonCyan
import com.example.friday.theme.StarkCrimson
import com.example.friday.theme.StarkGold
import com.example.friday.theme.StatusGreen
import com.example.friday.theme.TextCyan
import com.example.friday.theme.TextMuted

@Composable
fun PermissionControlDialog(
    permissions: List<PermissionItem>,
    onRequestAllPermissions: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val grantedCount = permissions.count { it.isGranted }
    val totalCount = permissions.size

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("permission_matrix_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BgDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyanBorderActive)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🛡",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "STARK ACCESS MATRIX",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NeonCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "DEVICE PERMISSION PROTOCOLS",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = StarkGold
                            )
                        }
                    }

                    Text(
                        text = "✕",
                        color = TextMuted,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable(onClick = onDismiss)
                            .padding(4.dp)
                            .testTag("close_permission_dialog")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Friday operates autonomously with your consent. Each subsystem grants specific capabilities:",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = TextCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                // List of Permissions
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    permissions.forEach { item ->
                        PermissionMatrixCard(item = item)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onRequestAllPermissions,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StarkGold,
                            contentColor = BgDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("dialog_authorize_all")
                    ) {
                        Text(
                            text = "AUTHORIZE ALL",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgCard,
                            contentColor = TextCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CyanBorder, RoundedCornerShape(8.dp))
                            .testTag("system_settings_btn")
                    ) {
                        Text(
                            text = "APP SETTINGS",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionMatrixCard(item: PermissionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgCard)
            .border(
                1.dp,
                if (item.isGranted) CyanBorder.copy(alpha = 0.5f) else StarkCrimson.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.icon,
                fontSize = 16.sp,
                color = if (item.isGranted) NeonCyan else StarkGold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = TextCyan
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.protocolCode,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        color = TextMuted
                    )
                }
                Text(
                    text = item.purpose,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = TextMuted,
                    lineHeight = 13.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (item.isGranted) StatusGreen.copy(alpha = 0.15f) else StarkCrimson.copy(alpha = 0.15f))
                .border(
                    1.dp,
                    if (item.isGranted) StatusGreen else StarkCrimson,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (item.isGranted) "LINKED" else "PENDING",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                color = if (item.isGranted) StatusGreen else StarkCrimson
            )
        }
    }
}
