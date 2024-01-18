package com.example.myapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.ui.camera.CameraFragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeImageAnalyzer(zoomCallback: CameraFragment.ZoomCallback?, val context: Context, val navController: NavController) : ImageAnalysis.Analyzer {
    private var barcodeScanner: BarcodeScanner

    init {
        barcodeScanner =
            if (zoomCallback != null){
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E)
                    .setZoomSuggestionOptions(ZoomSuggestionOptions.Builder(zoomCallback).build())
                    .build()
                BarcodeScanning.getClient(options)
            }
            else {
                BarcodeScanning.getClient()
            }
    }
    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener {
                    if (it.isNotEmpty()) {
                        for (barcode in it) {
                            Toast.makeText(
                                context,
                                "Barcode Number: ${barcode.displayValue}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(
                                BarcodeImageAnalyzer.TAG,
                                "Barcode Number: ${barcode.displayValue}"
                            )
                        }
                        navController.navigate(R.id.action_navigation_camera_to_navigation_addremove)
                    }
                }
        }
        imageProxy.close()
    }

    companion object{
        private const val TAG = "Barcode Image Analyzer"
    }
}