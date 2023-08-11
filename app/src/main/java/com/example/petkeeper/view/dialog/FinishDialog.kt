package com.example.petkeeper.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.FinishDialogBinding

class FinishDialog(private val context : AppCompatActivity) {
    private val binding = FinishDialogBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context)

    fun initDialog() {
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.yesButton.setOnClickListener {
            context.finish()
        }

        binding.noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}