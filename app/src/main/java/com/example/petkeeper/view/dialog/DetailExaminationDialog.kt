package com.example.petkeeper.view.dialog

import android.os.Bundle
import android.view.View
import com.example.petkeeper.R
import com.example.petkeeper.databinding.DetailExaminationDialogBinding
import com.example.petkeeper.util.binding.BindingFragment
import com.mcdev.quantitizerlibrary.AnimationStyle

class DetailExaminationDialog : BindingFragment<DetailExaminationDialogBinding>(R.layout.detail_examination_dialog, false) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.numberPicker?.textAnimationStyle = AnimationStyle.SLIDE_IN
    }
}