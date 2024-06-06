package com.room.layoutmanagerdemo.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.room.layoutmanagerdemo.R

class DragItemRecyclerAdapter : RecyclerView.Adapter<DragRecyclerVH>() {
    private val mDiffer: AsyncListDiffer<DragItemData> =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<DragItemData>() {

            override fun areItemsTheSame(oldItem: DragItemData, newItem: DragItemData): Boolean {
                return oldItem.iconUrl == newItem.iconUrl && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: DragItemData, newItem: DragItemData): Boolean {
                return oldItem.iconUrl == newItem.iconUrl && oldItem.name == newItem.name
            }

        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DragRecyclerVH {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_drag_demo, null)
        return DragRecyclerVH(item)
    }


    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<DragItemData>) {
        mDiffer.submitList(list)
    }

    override fun onBindViewHolder(holder: DragRecyclerVH, position: Int) {
        val bean = mDiffer.currentList[position]
        holder.itemView.findViewById<TextView>(R.id.tvItemName).text = bean.name
        Glide.with(holder.itemView).load(bean.iconUrl).into(holder.itemView.findViewById(R.id.imageViewItem))
    }
}