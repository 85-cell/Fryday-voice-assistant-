package com.example.friday.model

import androidx.compose.ui.graphics.Color

data class CommandPreset(
    val command: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val accentColor: Color
)
