package com.room.layoutmanagerdemo.menus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.room.layoutmanagerdemo.AppApplication
import com.room.layoutmanagerdemo.databinding.FragmentDemoMenusBinding
import com.room.layoutmanagerdemo.menus.model.MenuContent.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DemoItemRecyclerViewAdapter(
    private val values: List<MenuItem>
) : RecyclerView.Adapter<DemoItemRecyclerViewAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("DemoItemRecyclerViewAdapter", "onCreateViewHolder: ")
//        return ViewHolder(FragmentDemoMenusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        val holder = ViewHolder(AppApplication.demoMenusCache.take())
        AppApplication.preCreateViewCache()
        return holder
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