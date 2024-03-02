package com.example.myapplication.ui.addremove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentAddremoveBinding
import com.example.myapplication.model.InventoryItem

class AddRemoveFragment : Fragment() {

    private var _binding: FragmentAddremoveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: AddRemoveFragmentArgs by navArgs()

    private val viewModel: AddRemoveViewModel by viewModels {AddRemoveViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddremoveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.barcodeText.value = args.barcodeString

        val barcodeTextView: TextView = binding.barcodeText
        viewModel.barcodeText.observe(viewLifecycleOwner){
            barcodeTextView.text = it
        }

        val descriptionTextView: TextView = binding.descriptionText
        viewModel.descriptionText.observe(viewLifecycleOwner){
            descriptionTextView.text = it
        }

        binding.addButton.setOnClickListener {
            changeQuantityButton(1)
        }

        binding.subtractButton.setOnClickListener {
            changeQuantityButton(-1)
        }

        return root
    }

    private fun changeQuantityButton(quantity: Int) {
        viewModel.barcodeText.value = binding.barcodeText.text.toString()
        viewModel.descriptionText.value = binding.descriptionText.text.toString()

        viewModel.addItem(InventoryItem(null,
            viewModel.barcodeText.value!!,
            viewModel.descriptionText.value!!,
            quantity)) {
            val toastString = if (it?.id != null){
                if(quantity > 0) "Item added!" else "Item removed!"
            } else {
                if(quantity > 0) "Error adding item!" else "Error removing item"
            }

            Toast.makeText(requireContext(), toastString, Toast.LENGTH_SHORT).show()
        }

        viewModel.barcodeText.value = ""
        viewModel.descriptionText.value = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}