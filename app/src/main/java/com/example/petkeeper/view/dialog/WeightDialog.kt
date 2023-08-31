package com.example.petkeeper.view.dialog

import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.WeightDialogBinding

class WeightDialog(context: AppCompatActivity) {
    private val binding = WeightDialogBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context)

    fun initDialog(){
        dialog.apply {
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(binding.root)
                window?.apply {
                    setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            }.show()
        }

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initChart(){
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
        val weights = floatArrayOf(65f, 66f, 68f, 67f, 69f, 70f)
        binding.chart.apply {
            extraBottomOffset = 15f

        }
    }
}