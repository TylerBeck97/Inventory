package com.example.myapplication.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.InventoryAPI
import kotlinx.coroutines.launch

class InventoryViewModel : ViewModel() {

    private fun getItems() {
        viewModelScope.launch {
            val listResult = InventoryAPI.retrofitService.getItems()
        }
    }
}