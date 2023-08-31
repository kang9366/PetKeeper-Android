package com.example.petkeeper.view.dialog

import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.VaccinationDialogBinding

class VaccinationDialog(context: AppCompatActivity) {
    private val binding = VaccinationDialogBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context)

    fun initDialog(){
        binding.infoText.text = "현재 생후 16주차입니다. \n 현재 접종해야할 백신은 DHPPL 1차, 코로나 장염 1차 입니다. \n 접종 비용은 25000원 입니다."
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
}