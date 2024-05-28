package com.room.layoutmanagerdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.THSNestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.room.layoutmanagerdemo.ui.TonghuashunLinearLayout
import com.room.layoutmanagerdemo.ui.TonghuashunScrollViewTHS


class TonghuashunFragment : Fragment() {

    companion object {
        fun newInstance() = TonghuashunFragment()
    }

    private lateinit var viewModel: ThonghuashunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_thonghuashun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThonghuashunViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            Log.e("THS", "onLayout: contentView:,w = ${view.measuredWidth}, h = ${view.measuredHeight}")
            val targetView = view.findViewById<TonghuashunLinearLayout>(R.id.LinearLayoutMore);
            val layoutParams = targetView.layoutParams;
            layoutParams.width = MeasureSpec.makeMeasureSpec(view.measuredWidth, MeasureSpec.EXACTLY)
            layoutParams.height = MeasureSpec.makeMeasureSpec(view.measuredHeight, MeasureSpec.EXACTLY)
            targetView.requestLayout()
//            targetView.setTargetViewLayoutSize(view.measuredWidth,view.measuredHeight/2)
        }

        view.findViewById<TonghuashunScrollViewTHS>(R.id.scrollView).setOnScrollChangeListener(
            /**
             * Called when the scroll position of a view changes.
             * @param v The view whose scroll position has changed.
             * @param scrollX Current horizontal scroll origin.
             * @param scrollY Current vertical scroll origin.
             * @param oldScrollX Previous horizontal scroll origin.
             * @param oldScrollY Previous vertical scroll origin.
             */
            THSNestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.e(
                    "xxxx", String.format(
                        "OnScrollChangeListener: scrollX:%d,scrollY:%d,oldScrollX:%d,oldScrollY:%d",
                        scrollX,
                        scrollY,
                        oldScrollX,
                        oldScrollY
                    )
                )
                val txtView = v.findViewById<TextView>(R.id.tv_content)
                Log.e(
                    "xxxx", String.format(
                        "OnScrollChangeListener: l:%d", v.height - txtView.height * 2 + scrollY
                    )
                )
            })
        ViewCompat.setOnApplyWindowInsetsListener(
            view.rootView.rootView,
            object : OnApplyWindowInsetsListener {
                /**
                 * When [set][ViewCompat.setOnApplyWindowInsetsListener]
                 * on a View, this listener method will be called instead of the view's own
                 * `onApplyWindowInsets` method.
                 *
                 * @param v      The view applying window insets
                 * @param insets The insets to apply
                 * @return The insets supplied, minus any insets that were consumed
                 */
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val inset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                    val navigationBarHeight = insets.systemWindowInsetBottom
                    val paddingBottom = inset.bottom
                    val scrollView = view.findViewById<TonghuashunScrollViewTHS>(R.id.scrollView)
                    scrollView.setPadding(
                        scrollView.paddingLeft,
                        scrollView.paddingTop,
                        scrollView.paddingRight,
                        scrollView.bottom + paddingBottom
                    )
                    Log.e("xxxx", "onApplyWindowInsets: ${paddingBottom}")
                    return insets
                }
            })
    }

}