package com.example.myapplication.data

import com.example.myapplication.network.InventoryAPIService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val inventoryItemRepository: InventoryItemRepository
}

class DefaultAppContainer : AppContainer {
    private val BaseUrl = "http://10.0.2.2:8080"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BaseUrl)
        .build()

    private val retrofitService : InventoryAPIService by lazy {
        retrofit.create(InventoryAPIService::class.java)
    }

    override val inventoryItemRepository: InventoryItemRepository by lazy {
        NetworkInventoryItemRepository(retrofitService)
    }
}