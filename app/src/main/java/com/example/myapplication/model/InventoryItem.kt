package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val id: Long,
    val barcodeNumber: String,
    val description: String,
    val quantity: Int
)