package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val id: Long?,
    @SerialName("barcodeNumber")
    val barcode: String,
    val description: String,
    val quantity: Int
){
    override fun toString(): String {
        return "id = $id, barcodeNumber = $barcode, description = $description, quantity = $quantity"
    }

    fun incrementQuantity(): InventoryItem {
        return InventoryItem(id, barcode, description, quantity + 1)
    }

    fun decrementQuantity(): InventoryItem {
        return InventoryItem(id, barcode, description, quantity - 1)
    }
}