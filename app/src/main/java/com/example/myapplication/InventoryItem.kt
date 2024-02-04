package com.example.myapplication

class InventoryItem(name: String, barcodeNumber: Int) {
    private val name = name;
    private val barcodeNumber = barcodeNumber;

    fun getName(): String{
        return name
    }

    fun getBarcode(): Int{
        return barcodeNumber
    }
}