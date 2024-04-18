package com.room.layoutmanagerdemo.menus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.room.layoutmanagerdemo.databinding.FragmentDemoMenusBinding
import com.room.layoutmanagerdemo.menus.model.MenuContent.MenuItem


class DemoItemRecyclerViewAdapter(
    private val values: List<MenuItem>
) : RecyclerView.Adapter<DemoItemRecyclerViewAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentDemoMenusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.content
        holder.root.setOnClickListener { view ->
            onItemClickListener?.let {
                it.onItemClicked(view, position)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentDemoMenusBinding) : RecyclerView.ViewHolder(binding.root) {
        val root: View = binding.root
        val idView: ImageView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

}