package com.example.myapplication.network

import com.example.myapplication.model.InventoryItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path

interface InventoryAPIService {
    @GET("items")
    suspend fun getItems(): List<InventoryItem>

    @POST("items")
    fun addItem(@Body item: InventoryItem): Call<InventoryItem>

    @DELETE("items/{barcode}")
    fun deleteItem(@Path("barcode") barcode: String): Call<InventoryItem>

    @PUT("items/{barcode}")
    fun modifyItem(@Path("barcode") barcode: String, @Body item: InventoryItem): Call<InventoryItem>
}