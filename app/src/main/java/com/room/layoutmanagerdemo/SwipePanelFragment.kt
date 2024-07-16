package com.room.layoutmanagerdemo

import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.room.layoutmanagerdemo.databinding.FragmentSwipePanelBinding
import com.room.layoutmanagerdemo.ui.SwipePanel


class SwipePanelFragment : Fragment() {
    private lateinit var binding: FragmentSwipePanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSwipePanelBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipePanel.setOnFullSwipeListener { direction ->
            Toast.makeText(context, "$direction", Toast.LENGTH_LONG).show()
            if (direction == SwipePanel.TOP) {
                binding.swipePanel.close(true)
            }
        }
        binding.swipePanel.setOnProgressChangedListener { direction, progress, isTouch ->
            if (direction == SwipePanel.TOP) {
                Log.e("test", "setOnProgressChangedListener: $progress")
                val drawable = binding.swipePanel.getTopDrawable() as RotateDrawable
                drawable.setLevel((progress * 20000).toInt())
            }
        }
    }

    companion object {}
}