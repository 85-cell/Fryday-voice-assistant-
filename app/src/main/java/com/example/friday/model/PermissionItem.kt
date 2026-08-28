package com.example.friday.model

data class PermissionItem(
    val id: String,
    val protocolCode: String,
    val name: String,
    val purpose: String,
    val icon: String,
    val permissions: List<String>,
    val isGranted: Boolean = false
)
