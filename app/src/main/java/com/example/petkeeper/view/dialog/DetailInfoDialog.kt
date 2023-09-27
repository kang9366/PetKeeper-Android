package com.example.petkeeper.view.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.example.petkeeper.R
import com.example.petkeeper.databinding.DetailInfoDialogBinding
import com.example.petkeeper.util.binding.BindingFragment

class DetailInfoDialog : BindingFragment<DetailInfoDialogBinding>(R.layout.detail_info_dialog, false) {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.addDine?.setOnClickListener {
            val dialog = SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.dine_dialog_title)
            }.build().apply {
                setListener{ hour, minute ->
                    val textView = TextView(requireContext())
                    textView.setTextColor(Color.BLACK)
                    textView.text = "${hour}시 ${minute}분"
                    binding?.dineText?.addView(textView)
                }
            }
            fragmentManager?.let { it1 -> dialog.show(it1, tag) }
        }
    }
}