package com.example.myapplication.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.example.myapplication.model.InventoryItem


class InventoryFragment : Fragment() {

    private val viewModel: InventoryViewModel by viewModels {InventoryViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                Row {
                    Column (modifier = Modifier.fillMaxWidth(0.5f)){
                        Text("Barcode: ${item.barcode}")
                        Text(item.description)
                        Text("Quantity: ${item.quantity}")
                    }

                    QuantityButtons(true, item)
                    QuantityButtons(false, item)
                    DeleteButton(item)
                }
            Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    @Composable
    fun QuantityButtons(isIncrement: Boolean, item: InventoryItem){
        Button(onClick = {
            val modifiedItem = if (isIncrement) item.incrementQuantity() else item.decrementQuantity()
            viewModel.modifyItem(modifiedItem.barcode, modifiedItem) {
                val toastString = if (it?.id != null) {
                    if (isIncrement) "Item Incremented" else "Item Decremented"
                } else {
                    "Error"
                }
                Toast.makeText(requireContext(), toastString, Toast.LENGTH_SHORT).show()
                viewModel.getItems()
            }
        }) {
            if (isIncrement) Text("+") else Text("-")
        }
    }

    @Composable
    fun DeleteButton(item: InventoryItem){
        Button(onClick = {
            viewModel.deleteItem(item.barcode){
                val toastString = if (it?.id != null){
                    "Item deleted!"
                }
                else {
                    "Error removing item"
                }
                Toast.makeText(requireContext(), toastString, Toast.LENGTH_SHORT).show()
                viewModel.getItems()
            }
        }) {
            Text("X")
        }
    }
}