package com.room.layoutmanagerdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.room.layoutmanagerdemo.ui.scalelayout.ScaleLayout
import com.room.layoutmanagerdemo.ui.scalelayout.ScaleLayout.OnGetCanScaleListener
import com.room.layoutmanagerdemo.ui.scalelayout.TouchImageView

class ScaleFragment : Fragment() {

    private lateinit var viewModel: ScaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scale, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScaleViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mScaleLayout = view.findViewById(R.id.scale_layout) as ScaleLayout
        mScaleLayout.setSuggestScaleEnable(true)
        var touchImageView = view.findViewById(R.id.scaleLayout_center) as TouchImageView
        mScaleLayout.setOnGetCanScaleListener(OnGetCanScaleListener { !touchImageView.isZoomed })

        var mTop = view.findViewById(R.id.scaleLayout_top) as TextView
        mTop.setOnClickListener(View.OnClickListener { mScaleLayout.setState(ScaleLayout.STATE_CLOSE) })
    }

}