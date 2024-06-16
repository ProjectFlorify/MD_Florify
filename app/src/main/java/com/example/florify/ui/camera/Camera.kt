package com.example.florify.ui.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.florify.databinding.ActivityCameraBinding
import java.io.File

@Suppress("DEPRECATION")
class Camera : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private val aspectRatio = AspectRatio.RATIO_16_9
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var flashMode = ImageCapture.FLASH_MODE_OFF

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            val intent = Intent(this, CameraResult::class.java)
            intent.putExtra("imageUri", selectedImageUri.toString())
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener {
            takePhoto()
        }

        binding.flashButton.setOnClickListener {
            toggleFlash()
        }

        binding.rotateButton.setOnClickListener {
            switchCamera()
        }

        binding.galleryButton.setOnClickListener {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUserCases() {
        val preview = Preview.Builder()
            .setTargetAspectRatio(aspectRatio)
            .build().also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setFlashMode(flashMode)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun takePhoto() {
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            "CameraX_" + System.currentTimeMillis() + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val intent = Intent(this@Camera, CameraResult::class.java)
                    intent.putExtra("imageUri", savedUri.toString())
                    startActivity(intent)
                }
            }
        )
    }

    private fun toggleFlash() {
        flashMode = when (flashMode) {
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_OFF
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
            else -> ImageCapture.FLASH_MODE_OFF
        }
        bindCameraUserCases()
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        bindCameraUserCases()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
}
