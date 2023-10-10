package com.example.petkeeper.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.DialogBirthPickerBinding
import sh.tyy.wheelpicker.DatePickerView
import java.util.Calendar
import java.util.Date

interface PostDialogData {
    fun postData(data: String)
}

class BirthPickerDialog(private val context: AppCompatActivity) {
    private val binding = DialogBirthPickerBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context)
    private val calendar = Calendar.getInstance()

    fun initDialog(){
        setDialog()
        dialog.show()
        initDatePicker()
        initSetButton()
    }

    private fun initSetButton(){
        binding.setButton.setOnClickListener {
            val listener = context as PostDialogData
            listener.postData(binding.dateText.text.toString())
            dialog.dismiss()
        }
    }

    private fun initDatePicker(){
        binding.datePicker.apply {
            isHapticFeedbackEnabled = false
            maxDate = Date()
            setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            setWheelListener(object : DatePickerView.Listener {
                override fun didSelectData(year: Int, month: Int, day: Int) {
                    var realMonth = "${month+1}"
                    var realDayOfMonth = "$day"

                    if(month+1<10) {
                        realMonth ="0${month+1}"
                    }
                    if (day<10) {
                        realDayOfMonth = "0$day"
                    }

                    val selectDate = "${year}-${realMonth}-${realDayOfMonth}"
                    binding.dateText.text = selectDate
                }
            })
        }
    }

    private fun setDialog(){
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.BOTTOM)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }
}