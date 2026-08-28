package com.example.friday.model

enum class Speaker {
    USER,
    FRIDAY
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val speaker: Speaker,
    val text: String,
    val timestamp: String = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US).format(java.util.Date()),
    val systemActionType: String? = null
)
