package com.example.myapplication.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.myapplication.databinding.FragmentCameraBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import kotlinx.coroutines.launch
import com.example.myapplication.imageAnalysis.BarcodeImageAnalyzer

class CameraFragment : Fragment() {
    private lateinit var viewBinding: FragmentCameraBinding

    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permissions
        if (allPermissionsGranted()) {
            lifecycleScope.launch {
                startCamera()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCameraBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        viewBinding.viewFinder.post {
            lifecycleScope.launch {
                startCamera()
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeImageAnalyzer(ZoomCallback(camera),
                        requireContext(), requireView().findNavController())
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer)

                preview.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraFragment"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(context,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    startCamera()
                }
            }
        }

    class ZoomCallback(val camera: Camera?) : ZoomSuggestionOptions.ZoomCallback{
        override fun setZoom(zoomRatio: Float): Boolean {
            camera?.cameraControl?.setZoomRatio(zoomRatio)
            return true
        }
    }
}