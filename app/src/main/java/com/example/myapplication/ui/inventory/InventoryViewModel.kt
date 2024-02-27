package com.example.myapplication.ui.inventory

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.InventoryItem
import com.example.myapplication.network.InventoryAPI
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Error

sealed interface InventoryUIState {
    data class Success(val items: List<InventoryItem>) : InventoryUIState

    data class Error(val error: Exception) : InventoryUIState

    object Loading : InventoryUIState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class InventoryViewModel : ViewModel() {

    var inventoryUIState: InventoryUIState by mutableStateOf(InventoryUIState.Loading)
        private set

    val TAG = "InventoryViewModel"

    init {
        getItems()
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getItems() {
        viewModelScope.launch {
            Log.d(TAG, "getItems: Trying to get items from server")
            inventoryUIState = InventoryUIState.Loading
            inventoryUIState = try {
                val listResult = InventoryAPI.retrofitService.getItems()
                Log.d(TAG, "getItems: Success getting items from server")
                InventoryUIState.Success(listResult)

            }
            catch (e: IOException){
                Log.d(TAG, "getItems: Something went wrong IOException")
                InventoryUIState.Error(e)
            }
            catch (e: HttpException){
                Log.d(TAG, "getItems: Something went wrong HttpException")
                InventoryUIState.Error(e)
            }
        }
    }
}