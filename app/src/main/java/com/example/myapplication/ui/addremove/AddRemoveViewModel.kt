package com.example.myapplication.ui.addremove

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.InventoryApplication
import com.example.myapplication.data.InventoryItemRepository
import com.example.myapplication.model.InventoryItem

class AddRemoveViewModel(private val inventoryItemRepository: InventoryItemRepository) : ViewModel() {

    private val _barcodeText = MutableLiveData<String>().apply {
        value = ""
    }

    private val _descriptionText = MutableLiveData<String>().apply {
        value = ""
    }

    var barcodeText: MutableLiveData<String> = _barcodeText
    var descriptionText: MutableLiveData<String> = _descriptionText

    fun addItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit){
        inventoryItemRepository.addInventoryItem(item, onResult)
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