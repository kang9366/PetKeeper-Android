package com.example.petkeeper.view.dialog

import android.os.Bundle

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petkeeper.R
import com.example.petkeeper.databinding.DetailExaminationDialogBinding
import com.example.petkeeper.util.binding.BindingFragment
import com.mcdev.quantitizerlibrary.AnimationStyle
import com.mcdev.quantitizerlibrary.QuantitizerListener

class DetailExaminationDialog : BindingFragment<DetailExaminationDialogBinding>(R.layout.detail_examination_dialog, false) {
    private val viewModel: TestViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.numberPicker?.textAnimationStyle = AnimationStyle.SLIDE_IN

        binding?.numberPicker?.setQuantitizerListener(object : QuantitizerListener{
            override fun onDecrease() {
            }

            override fun onIncrease() {
            }

            override fun onValueChanged(value: Int) {
                viewModel.updateWeight(binding?.numberPicker?.value.toString())
            }
        })
    }
}

class TestViewModel(): ViewModel(){
    private val _weight = MutableLiveData<String>()
    val weight: LiveData<String>
        get() = _weight

    fun updateWeight(newWeight: String) {
        _weight.value = newWeight
    }
}