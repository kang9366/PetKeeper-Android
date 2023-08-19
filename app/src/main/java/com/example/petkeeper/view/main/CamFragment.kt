package com.example.petkeeper.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.petkeeper.R
import com.example.petkeeper.databinding.FragmentCamBinding
import com.example.petkeeper.util.binding.BindingFragment
import com.google.common.util.concurrent.ListenableFuture

class CamFragment : BindingFragment<FragmentCamBinding>(R.layout.fragment_cam, true) {
    companion object {
        val PERMISSION_REQUEST_CODE = 1000
    }
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder()
            .build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding?.previewView?.surfaceProvider)

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            println("s")
        }else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }
    }
}