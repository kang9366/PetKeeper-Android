package com.example.petkeeper.util.adapter

import android.view.View

interface OnItemClickListener{
    fun onItemClick(v: View, data: DateItem, pos : Int)
}