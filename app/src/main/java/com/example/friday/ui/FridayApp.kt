package com.example.friday.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.friday.theme.BgDark
import com.example.friday.ui.components.ActivityLog
import com.example.friday.ui.components.CommandConsole
import com.example.friday.ui.components.ConversationalStream
import com.example.friday.ui.components.CoreVisualizer
import com.example.friday.ui.components.HudReadout
import com.example.friday.ui.components.PermissionControlDialog
import com.example.friday.ui.components.QuickCommands
import com.example.friday.ui.components.SettingsDialog
import com.example.friday.ui.components.StarkProtocolBanner
import com.example.friday.ui.components.TopBar
import com.example.friday.viewmodel.FridayViewModel

@Composable
fun FridayApp(
    viewModel: FridayViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Gather all android permissions from the permission list
    val allRequiredPermissions = remember {
        val base = mutableListOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            base.add(android.Manifest.permission.POST_NOTIFICATIONS)
            base.add(android.Manifest.permission.READ_MEDIA_IMAGES)
            base.add(android.Manifest.permission.READ_MEDIA_AUDIO)
            base.add(android.Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            base.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        base.toTypedArray()
    }

    // Permission Launcher for Stark Handshake
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        viewModel.onPermissionsResult(results)
    }

    // Auto toast handler
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BgDark,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(BgDark)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Bar
            TopBar(
                voiceMuted = uiState.voiceMuted,
                allPermissionsGranted = uiState.allPermissionsGranted,
                onToggleMute = { viewModel.toggleVoiceMuted() },
                onOpenPermissions = { viewModel.setShowPermissionModal(true) },
                onOpenSettings = { viewModel.setShowSettings(true) }
            )

            // Stark Protocol 01: All-Phone Access Handshake
            StarkProtocolBanner(
                permissions = uiState.permissions,
                allGranted = uiState.allPermissionsGranted,
                onRequestAllPermissions = {
                    permissionLauncher.launch(allRequiredPermissions)
                },
                onOpenPermissionDetails = {
                    viewModel.setShowPermissionModal(true)
                }
            )

            // Holographic Arc Reactor Visualizer
            CoreVisualizer(
                isListening = uiState.isListening,
                isSpeaking = uiState.isSpeaking,
                isProcessing = uiState.isProcessing,
                audioLevel = uiState.audioLevel,
                onCoreClick = {
                    viewModel.toggleListening()
                }
            )

            // HUD Telemetry Readout
            HudReadout(
                cpuTemp = uiState.cpuTemp,
                securityStatus = uiState.securityStatus,
                latencyMs = uiState.latencyMs,
                batteryLevel = uiState.batteryLevel,
                freeStorageGb = uiState.freeStorageGb
            )

            // Live Voice Command Console
            CommandConsole(
                isListening = uiState.isListening,
                transcript = uiState.transcript,
                onToggleListening = { viewModel.toggleListening() },
                onSubmitCommand = { text -> viewModel.executeUserSpeech(text) }
            )

            // Conversational Stream
            ConversationalStream(
                messages = uiState.chatMessages,
                onAuthorizeClick = {
                    permissionLauncher.launch(allRequiredPermissions)
                },
                onSpeakReply = { text ->
                    viewModel.speak(text)
                }
            )

            // Quick Autonomous Commands
            QuickCommands(
                isTorchOn = uiState.isTorchOn,
                batteryLevel = uiState.batteryLevel,
                onExecuteCommand = { cmd -> viewModel.executeUserSpeech(cmd) },
                onToggleTorch = { viewModel.toggleFlashlight() }
            )

            // Audit Trail / Activity Log
            ActivityLog(
                history = uiState.history,
                onUndoClick = { item -> viewModel.undoAction(item) },
                onClearAllClick = { viewModel.clearAllHistory() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Modals & Dialogs
        if (uiState.showPermissionModal) {
            PermissionControlDialog(
                permissions = uiState.permissions,
                onRequestAllPermissions = {
                    permissionLauncher.launch(allRequiredPermissions)
                },
                onDismiss = { viewModel.setShowPermissionModal(false) }
            )
        }

        if (uiState.showSettings) {
            SettingsDialog(
                continuousListening = uiState.continuousListening,
                hapticsEnabled = uiState.hapticsEnabled,
                speechRate = uiState.speechRate,
                speechPitch = uiState.speechPitch,
                onContinuousListeningChange = { viewModel.updateContinuousListening(it) },
                onHapticsChange = { viewModel.updateHaptics(it) },
                onSpeechRateChange = { viewModel.updateSpeechRate(it) },
                onSpeechPitchChange = { viewModel.updateSpeechPitch(it) },
                onDismiss = { viewModel.setShowSettings(false) }
            )
        }
    }
}
