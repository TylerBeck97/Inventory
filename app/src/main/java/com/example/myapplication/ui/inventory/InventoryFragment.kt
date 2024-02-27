package com.example.myapplication.ui.inventory

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.Text
import com.example.myapplication.model.InventoryItem


class InventoryFragment : Fragment() {

    private lateinit var viewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[InventoryViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                when (viewModel.inventoryUIState){
                    is InventoryUIState.Loading -> LoadingScreen()
                    is InventoryUIState.Success -> ResultScreen((viewModel.inventoryUIState as InventoryUIState.Success).items)
                    is InventoryUIState.Error -> ErrorScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getItems()
    }

    @Composable
    fun ErrorScreen(){
        Text("Error: ${(viewModel.inventoryUIState as InventoryUIState.Error).error}")
    }
    @Composable
    fun LoadingScreen(){
        Text("Loading")
    }

    @Composable
    fun ResultScreen(items: List<InventoryItem>) {
        LazyColumn {
            items(items) {item ->
                Text(item.toString())
            }
        }
    }
}