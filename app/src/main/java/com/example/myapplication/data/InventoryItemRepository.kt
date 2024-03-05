package com.example.myapplication.data

import com.example.myapplication.model.InventoryItem
import com.example.myapplication.network.InventoryAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface InventoryItemRepository {
    suspend fun getItems(): List<InventoryItem>

    suspend fun getItem(barcode: String): InventoryItem

    fun addInventoryItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit)

    fun deleteItem(barcode: String, onResult: (InventoryItem?) -> Unit)

    fun modifyItem(barcode: String, item: InventoryItem, onResult: (InventoryItem?) -> Unit)
}

class NetworkInventoryItemRepository(
    private val inventoryApiService: InventoryAPIService
): InventoryItemRepository{
    override suspend fun getItems(): List<InventoryItem> {
        return inventoryApiService.getItems()
    }

    override suspend fun getItem(barcode: String): InventoryItem {
        return inventoryApiService.getItem(barcode)
    }

    override fun addInventoryItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit) {
        return inventoryApiService.addItem(item).enqueue(object : Callback<InventoryItem> {
            override fun onFailure(call: Call<InventoryItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<InventoryItem>, response: Response<InventoryItem>
            ) {
                val addedItem = response.body()
                onResult(addedItem)
            }
        })
    }

    override fun deleteItem(barcode: String, onResult: (InventoryItem?) -> Unit){
        return inventoryApiService.deleteItem(barcode).enqueue(
            object : Callback<InventoryItem> {
                override fun onFailure(call: Call<InventoryItem>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<InventoryItem>, response: Response<InventoryItem>
                ) {
                    val addedItem = response.body()
                    onResult(addedItem)
                }
        })
    }

    override fun modifyItem(barcode: String, item: InventoryItem, onResult: (InventoryItem?) -> Unit) {
        return inventoryApiService.modifyItem(barcode, item).enqueue(object : Callback<InventoryItem> {
                override fun onFailure(call: Call<InventoryItem>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<InventoryItem>, response: Response<InventoryItem>
                ) {
                    val addedItem = response.body()
                    onResult(addedItem)
                }
        })
    }

}