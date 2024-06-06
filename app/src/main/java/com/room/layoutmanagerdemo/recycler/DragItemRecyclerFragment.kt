package com.room.layoutmanagerdemo.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.room.layoutmanagerdemo.R
import java.util.Collections


class DragItemRecyclerFragment : Fragment() {

    companion object {
        fun newInstance() = DragItemRecyclerFragment()
    }

    private lateinit var viewModel: DragItemRecyclerViewModel

    private var enableMove = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drag_item_recycler, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DragItemRecyclerViewModel::class.java]
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.let {
            val adapter = DragItemRecyclerAdapter()
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
            viewModel.dragItemLiveData.observe(viewLifecycleOwner) { beans ->
                adapter.submitList(beans)
            }

            // 1. 创建 ItemTouchHelper.Callback 实现
            val callback: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {
                // 设置拖拽和滑动的类型
                override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    // 允许水平拖拽
                    var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    // 允许滑动
                    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    if (!enableMove) {
                        dragFlags = 0
                    }
                    return makeMovementFlags(dragFlags, 0)
                }

                // 处理拖拽事件
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    // 更新数据源以反映新的顺序
                    Collections.swap(
                        viewModel.dragItemLiveData.value,
                        viewHolder.absoluteAdapterPosition,
                        target.absoluteAdapterPosition
                    )
                    // 通知适配器数据已更改
                    adapter.notifyItemMoved(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
                    return true
                }


                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            }

            // 2. 创建 ItemTouchHelper 实例
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(it)
        }
        viewModel.dragItemLiveData.value = listOf(
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test", "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg"),
            DragItemData("Test1", "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg")
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            enableMove = !enableMove
            if (enableMove) {
                Toast.makeText(context, "Enable Move", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Disable Move", Toast.LENGTH_SHORT).show()
            }
        }
    }
}