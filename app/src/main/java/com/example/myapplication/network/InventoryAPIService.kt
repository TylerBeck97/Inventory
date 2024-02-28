package com.example.myapplication.network

import com.example.myapplication.model.InventoryItem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call

private const val BASE_URL = "http://10.0.2.2:8080"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface InventoryAPIService {
    @GET("items")
    suspend fun getItems(): List<InventoryItem>

    @POST("items")
    fun addItem(@Body item: InventoryItem): Call<InventoryItem>

    @DELETE("items")
    fun removeItem()
}

object InventoryAPI {
    val retrofitService : InventoryAPIService by lazy {
        retrofit.create(InventoryAPIService::class.java)
    }
}