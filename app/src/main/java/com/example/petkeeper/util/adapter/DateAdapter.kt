package com.example.petkeeper.util.adapter

import android.util.Log
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
    private var listener : OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        if (position == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1) {
            Log.d("testtt", position.toString())
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.button_color))
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val day: TextView = view.findViewById(R.id.day)
        val date: TextView = view.findViewById(R.id.date)

        fun bind(item: DateItem){
            day.text = item.day
            date.text = item.date.toString()
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
}