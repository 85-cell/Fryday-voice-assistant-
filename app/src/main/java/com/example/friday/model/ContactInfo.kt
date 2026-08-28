package com.example.friday.model

data class ContactInfo(
    val name: String,
    val number: String,
    val lookupKey: String? = null
)
