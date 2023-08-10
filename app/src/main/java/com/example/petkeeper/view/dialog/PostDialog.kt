package com.example.petkeeper.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.petkeeper.databinding.PostDialogBinding

class PostDialog(val context: FragmentActivity) {
    private lateinit var binding: PostDialogBinding
    private val dialog = Dialog(context)
    private var cameraCallback: (() -> Unit)? = null

    fun initDialog(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>, cameraCallback: () -> Unit){
        binding = PostDialogBinding.inflate(context.layoutInflater)

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.BOTTOM)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

        binding.runGallery.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.runCamera.setOnClickListener {
            cameraCallback.invoke()
        }
        dialog.show()
    }

    fun closeDialog() = dialog.dismiss()
}