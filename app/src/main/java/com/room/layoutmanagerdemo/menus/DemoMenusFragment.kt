package com.room.layoutmanagerdemo.menus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.room.layoutmanagerdemo.R
import com.room.layoutmanagerdemo.menus.model.MenuContent

/**
 * A fragment representing a list of Items.
 */
class DemoMenusFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_demo_menus_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> GridLayoutManager(context, 3)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = DemoItemRecyclerViewAdapter(MenuContent.ITEMS)
//                val dockerView = findViewById<View>(android.R.id.content)
//                val w = dockerView.width / 3
                (adapter as DemoItemRecyclerViewAdapter).let {
                    it.onItemClickListener =
                        object : DemoItemRecyclerViewAdapter.OnItemClickListener {
                            override fun onItemClicked(view: View, position: Int) {
                                findNavController().navigate(MenuContent.ITEMS[position].resId)
                            }
                        }
                }
            }
        }
        return view
    }
}