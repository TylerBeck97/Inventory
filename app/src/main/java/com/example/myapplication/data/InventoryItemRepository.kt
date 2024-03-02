package com.example.myapplication.data

import com.example.myapplication.model.InventoryItem
import com.example.myapplication.network.InventoryAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface InventoryItemRepository {
    suspend fun getInventoryItems(): List<InventoryItem>

    fun addInventoryItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit)
}

class NetworkInventoryItemRepository(
    private val inventoryApiService: InventoryAPIService
): InventoryItemRepository{
    override suspend fun getInventoryItems(): List<InventoryItem> {
        return inventoryApiService.getItems()
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

}