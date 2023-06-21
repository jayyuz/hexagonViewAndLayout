package com.room.roomwordsample.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.room.layoutmanagerdemo.R
import com.room.layoutmanagerdemo.ui.HexagonView

class HexagonLayoutManagerAdapter : RecyclerView.Adapter<HexagonLayoutManagerViewHolder>() {
    private val TAG = "LayoutManagerAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HexagonLayoutManagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_menu_item_hexagon, parent, false)
        return HexagonLayoutManagerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 99
    }

    override fun onBindViewHolder(holder: HexagonLayoutManagerViewHolder, position: Int) {
        holder.textView.text = "$position"
        holder.item.setCardColor(Color.RED)
        holder.item.tag = position
        holder.item.setOnClickListener {
            Log.e(TAG, "onBindViewHolder:${it.tag}")
        }
    }
}

class HexagonLayoutManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.textView)
    val item: HexagonView = itemView.findViewById(R.id.item)
}