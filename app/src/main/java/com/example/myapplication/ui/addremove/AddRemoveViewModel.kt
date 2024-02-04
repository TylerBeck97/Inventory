package com.example.myapplication.ui.addremove

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddRemoveViewModel : ViewModel() {

    private val _barcodeText = MutableLiveData<String>().apply {
        value = ""
    }

    private val _descriptionText = MutableLiveData<String>().apply {
        value = ""
    }

    var barcodeText: MutableLiveData<String> = _barcodeText
    val descriptionText: LiveData<String> = _descriptionText
}