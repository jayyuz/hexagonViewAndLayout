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
    private var data: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HexagonLayoutManagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_menu_item_hexagon, parent, false)
        return HexagonLayoutManagerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun add(values: List<String>) {
        val index = data.lastIndex
        data.addAll(values)
        notifyItemInserted(index)
    }

    fun add(value: String) {
        data.add(value)
        notifyItemInserted(data.lastIndex)
    }

    fun insert(index: Int, value: String) {
        data.add(index, value)
        notifyItemInserted(index)
    }

    fun move(from: Int, to: Int) {
        val t = to.takeIf { it <= data.lastIndex }
        t?.let {
            val value = data.removeAt(from)
            data.add(t, value)
            notifyItemMoved(from, to)
        }
    }

    fun remove(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onBindViewHolder(holder: HexagonLayoutManagerViewHolder, position: Int) {
        holder.textView.text = data[position]
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