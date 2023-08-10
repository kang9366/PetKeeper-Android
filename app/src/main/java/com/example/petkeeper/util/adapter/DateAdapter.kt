package com.example.petkeeper.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petkeeper.R
import java.util.Calendar

data class DateItem(val date: Int, val day: String)

class DateAdapter(private val items: ArrayList<DateItem>): RecyclerView.Adapter<DateAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.day.text = item.day
        holder.date.text = item.date.toString()

        if (position == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_color))
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val day: TextView = view.findViewById<TextView>(R.id.day)
        val date: TextView = view.findViewById<TextView>(R.id.date)
    }
}