package com.example.myapplication.ui.addremove

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.InventoryItem
import com.example.myapplication.network.InventoryAPI
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRemoveViewModel : ViewModel() {

    private val _barcodeText = MutableLiveData<String>().apply {
        value = ""
    }

    private val _descriptionText = MutableLiveData<String>().apply {
        value = ""
    }

    var barcodeText: MutableLiveData<String> = _barcodeText
    var descriptionText: MutableLiveData<String> = _descriptionText

    fun addItem(item: InventoryItem, onResult: (InventoryItem?) -> Unit){
        val retrofit = InventoryAPI.retrofitService.addItem(item).enqueue(
            object : Callback<InventoryItem> {
                override fun onFailure(call: Call<InventoryItem>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<InventoryItem>, response: Response<InventoryItem>
                ) {
                    val addedItem = response.body()
                    onResult(addedItem)
                }
            }
        )


    }
}