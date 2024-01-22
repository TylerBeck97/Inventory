package com.example.myapplication.ui.addremove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addRemoveViewModel =
            ViewModelProvider(this).get(AddRemoveViewModel::class.java)

        _binding = FragmentAddremoveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val barcodeTextView: TextView = binding.barcodeText
        addRemoveViewModel.barcodeText.observe(viewLifecycleOwner){
            barcodeTextView.text = args.barcodeString
        }

        val descriptionTextView: TextView = binding.descriptionText
        addRemoveViewModel.descriptionText.observe(viewLifecycleOwner){
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
        TODO("Not yet implemented")
    }

    private fun addButton() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}