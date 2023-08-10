package com.example.petkeeper.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.petkeeper.databinding.DetailDialogBinding
import com.example.petkeeper.util.adapter.DateItem
import com.example.petkeeper.util.adapter.ViewPagerAdapter

class DetailDialog(private val context : AppCompatActivity) {
    private lateinit var binding: DetailDialogBinding
    private val dialog = Dialog(context)

    @SuppressLint("SetTextI18n")
    fun initDialog(year: Int, month: Int, day: DateItem){
        binding = DetailDialogBinding.inflate(context.layoutInflater)
        val viewPagerAdapter = ViewPagerAdapter(context)
        viewPagerAdapter.addFragment(DetailInfoDialog())
        viewPagerAdapter.addFragment(DetailExaminationDialog())

        binding.viewPager.apply {
            adapter = viewPagerAdapter
        }
        binding.indicator.setViewPager(binding.viewPager)

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }.show()

        binding.titleText.text = "${year}년 ${month}월 ${day.day}요일"

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}