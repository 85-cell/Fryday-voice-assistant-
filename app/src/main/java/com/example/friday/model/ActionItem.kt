package com.example.friday.model

data class ActionItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val icon: String,
    val title: String,
    val detail: String,
    val time: String,
    val canUndo: Boolean = false
)
