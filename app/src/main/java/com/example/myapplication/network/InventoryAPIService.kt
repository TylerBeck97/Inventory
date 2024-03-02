package com.example.myapplication.network

import com.example.myapplication.model.InventoryItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call

interface InventoryAPIService {
    @GET("items")
    suspend fun getItems(): List<InventoryItem>

    @POST("items")
    fun addItem(@Body item: InventoryItem): Call<InventoryItem>

    @DELETE("items")
    fun removeItem()
}