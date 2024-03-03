package com.example.myapplication.ui.inventory

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.InventoryApplication
import com.example.myapplication.data.InventoryItemRepository
import com.example.myapplication.model.InventoryItem
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface InventoryUIState {
    data class Success(val items: List<InventoryItem>) : InventoryUIState

    data class Error(val error: Exception) : InventoryUIState

    object Loading : InventoryUIState
}

class InventoryViewModel(private val inventoryItemRepository: InventoryItemRepository) : ViewModel() {

    var inventoryUIState: InventoryUIState by mutableStateOf(InventoryUIState.Loading)
        private set

    val TAG = "InventoryViewModel"

    init {
        getItems()
    }

    fun getItems() {
        viewModelScope.launch {
            Log.d(TAG, "getItems: Trying to get items from server")
            inventoryUIState = InventoryUIState.Loading
            inventoryUIState = try {
                val listResult = inventoryItemRepository.getInventoryItems()
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

    fun deleteItem(barcode: String, onResult: (InventoryItem?) -> Unit){
        inventoryItemRepository.deleteItem(barcode, onResult)
    }

    fun modifyItem(barcode: String, item: InventoryItem, onResult: (InventoryItem?) -> Unit){
        inventoryItemRepository.modifyItem(barcode, item, onResult)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryItemRepository = application.container.inventoryItemRepository
                InventoryViewModel(inventoryItemRepository = inventoryItemRepository)
            }
        }
    }
}