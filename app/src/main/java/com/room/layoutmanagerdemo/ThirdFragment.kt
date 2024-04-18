package com.room.layoutmanagerdemo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.alpha
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.MaterialToolbar
import com.room.layoutmanagerdemo.databinding.FragmentThirdBinding

class ThirdFragment : Fragment() {

    companion object {
        fun newInstance() = ThirdFragment()
    }

    private lateinit var viewModel: ThirdViewModel
    private var _binding: FragmentThirdBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThirdViewModel::class.java)
//        (@NonNull NestedScrollView v, int scrollX, int scrollY,
//            int oldScrollX, int oldScrollY)->{
//
//        }
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView,
                                                             scrollX: Int,
                                                             scrollY: Int,
                                                             oldScrollX: Int,
                                                             oldScrollY: Int
            ->

            var offset = binding.customScrollingView.y - scrollY
            var alpha = 1f
            if (offset >= 0) {
                alpha = offset / binding.customScrollingView.y
            } else {
                alpha = 0f
            }
            if (requireActivity() is MainActivity) {
                val toolbar: MaterialToolbar = (requireActivity() as MainActivity).getToolBar()
                if (alpha > 0) {
                    toolbar.background = ColorDrawable(Color.TRANSPARENT)
                } else {
                    toolbar.background = ColorDrawable(Color.WHITE)
                }
            }
            binding.flag.alpha = alpha
            Log.e("offset", "onActivityCreated: $offset")
        }

    }

}