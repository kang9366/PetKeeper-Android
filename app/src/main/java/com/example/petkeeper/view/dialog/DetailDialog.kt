package com.example.petkeeper.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.DetailDialogBinding
import com.example.petkeeper.util.adapter.DateItem
import com.example.petkeeper.util.adapter.ViewPagerAdapter

class DetailDialog(context : AppCompatActivity) {
    private val binding = DetailDialogBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context)
    private val viewPagerAdapter = ViewPagerAdapter(context)

    @SuppressLint("SetTextI18n")
    fun initDialog(year: Int, month: Int, day: DateItem){
        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)
        binding.titleText.text = "${year}년 ${month-1}월 ${day.date}일"

        viewPagerAdapter.apply {
            addFragment(DetailInfoDialog())
            addFragment(DetailExaminationDialog())
        }

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }.show()

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}