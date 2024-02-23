package com.example.myapplication.ui.addremove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentAddremoveBinding

class AddRemoveFragment : Fragment() {

    private var _binding: FragmentAddremoveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: AddRemoveFragmentArgs by navArgs()

    private lateinit var viewModel: AddRemoveViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddremoveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel =
            ViewModelProvider(this)[AddRemoveViewModel::class.java]
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
            addButton()
        }

        binding.subtractButton.setOnClickListener {
            subButton()
        }

        return root
    }

    private fun subButton() {
        Toast.makeText(requireContext(), "Sub Button: ${viewModel.descriptionText.value}",
            Toast.LENGTH_SHORT).show()
    }

    private fun addButton() {
        viewModel.barcodeText.value = binding.barcodeText.text.toString()
        viewModel.descriptionText.value = binding.descriptionText.text.toString()

        Toast.makeText(requireContext(), "Add Button: ${viewModel.barcodeText.value}",
            Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "Add Button: ${viewModel.descriptionText.value}",
            Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "Clearing Field",
            Toast.LENGTH_SHORT).show()

        viewModel.barcodeText.value = ""
        viewModel.descriptionText.value = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}