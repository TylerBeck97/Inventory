package com.example.myapplication.ui.addremove

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.InventoryApplication
import com.example.myapplication.data.InventoryItemRepository
import com.example.myapplication.model.InventoryItem
import kotlinx.coroutines.launch

class AddRemoveViewModel(private val inventoryItemRepository: InventoryItemRepository) : ViewModel() {

    private val _barcodeText = MutableLiveData<String>().apply {
        value = ""
    }

    private val _descriptionText = MutableLiveData<String>().apply {
        value = ""
    }

    private val TAG = "AddRemoveViewModel"

    var barcodeText: MutableLiveData<String> = _barcodeText
    var descriptionText: MutableLiveData<String> = _descriptionText

    fun addItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit){
        inventoryItemRepository.addInventoryItem(item, onResult)
    }

    fun getItem(barcode: String){
        viewModelScope.launch {
            descriptionText.value = ""
            descriptionText.value = try {
                inventoryItemRepository.getItem(barcode).description
            }
            catch (e: Exception){
                Log.d(TAG, e.toString())
                ""
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
                val inventoryItemRepository = application.container.inventoryItemRepository
                AddRemoveViewModel(inventoryItemRepository = inventoryItemRepository)
            }
        }
    }
}