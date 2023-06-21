package com.room.roomwordsample.layout.manager

import android.graphics.Rect
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.room.layoutmanagerdemo.ui.Pool
import kotlin.math.ceil
import kotlin.math.max

class HexagonLayoutManager(private val mGroupSize: Int = 5, private val mAlignCenter: Boolean = true) :
    RecyclerView.LayoutManager() {

    private val H3 = 0.865f

    private var mHorizontalOffset = 0
    private var mVerticalOffset = 0
    private var mTotalWidth = 0
    private var mTotalHeight = 0
    private var mGravityOffset = 0
    private val mItemFrame by lazy {
        Pool(Pool.New {
            return@New Rect()
        })
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount <= 0 || state?.isPreLayout == true || recycler == null) {
            return
        }
        detachAndScrapAttachedViews(recycler)
        val firstView = recycler.getViewForPosition(0)
        measureChildWithMargins(firstView, 0, 0)
        val itemWidth = getDecoratedMeasuredWidth(firstView)
        val itemHeight = getDecoratedMeasuredHeight(firstView)

        val firstLineSize = mGroupSize / 2 + 1
        val firstLineWith = firstLineSize * itemWidth * 3 / 2 - itemWidth / 2
        mGravityOffset = if (mAlignCenter && firstLineWith < getHorizontalSpace()) {
            (getHorizontalSpace() - firstLineWith) / 2
        } else {
            0
        }

        var baseLeftOffset = 0
        val lineHeight = itemHeight * H3
        for (i in 0 until itemCount) {
            val item: Rect = mItemFrame[i]
            var offsetHeight = (i / mGroupSize * lineHeight).toInt()

            val offset = i % mGroupSize
            if (isItemInFirstLine(i)) {
                baseLeftOffset = mGravityOffset + offset * (itemWidth + itemWidth / 2)
                item.set(baseLeftOffset, offsetHeight, baseLeftOffset + itemWidth, offsetHeight + itemHeight)
            } else {
                offsetHeight = (offsetHeight + lineHeight / 2).toInt()
                baseLeftOffset =
                    mGravityOffset + (offset - firstLineSize) * (itemWidth + itemWidth / 2) + itemWidth - itemWidth / 4
                item.set(baseLeftOffset, offsetHeight, baseLeftOffset + itemWidth, offsetHeight + itemHeight)
            }
        }

        mTotalWidth = max(firstLineWith, getHorizontalSpace())
        var totalHeight = getGroupSize() * lineHeight
        if (!isItemInFirstLine(itemCount - 1)) {
            totalHeight += lineHeight
        }
        mTotalHeight = max(totalHeight.toInt(), getVerticalSpace())

        layoutChildren(recycler, state)
    }

    private fun layoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        if (itemCount <= 0 || state!!.isPreLayout) {
            return
        }
        val displayRect = Rect(
            mHorizontalOffset,
            mVerticalOffset,
            getHorizontalSpace() + mHorizontalOffset,
            getVerticalSpace() + mVerticalOffset
        )

        for (i in 0 until itemCount) {
            val frame: Rect = mItemFrame[i]
            // 判断如果在显示区域，则将其显示布局显示出来
            if (Rect.intersects(displayRect, frame)) {
                val scrap = recycler.getViewForPosition(i)
                addView(scrap)
                measureChildWithMargins(scrap, 0, 0)
                layoutDecorated(
                    scrap,
                    frame.left - mHorizontalOffset,
                    frame.top - mVerticalOffset,
                    frame.right - mHorizontalOffset,
                    frame.bottom - mVerticalOffset
                )
            }
        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        var ndy = dy
        detachAndScrapAttachedViews(recycler!!)
        if (mVerticalOffset + ndy < 0) {
            ndy = -mVerticalOffset
        } else if (mVerticalOffset + ndy > mTotalHeight - getVerticalSpace()) {
            ndy = mTotalHeight - getVerticalSpace() - mVerticalOffset
        }
        offsetChildrenVertical(-ndy)
        layoutChildren(recycler, state)
        mVerticalOffset += ndy
        return ndy
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        var ndx = dx
        detachAndScrapAttachedViews(recycler!!)
        if (mHorizontalOffset + ndx < 0) {
            ndx = -mHorizontalOffset
        } else if (mHorizontalOffset + ndx > mTotalWidth - getHorizontalSpace()) {
            ndx = mTotalWidth - getHorizontalSpace() - mHorizontalOffset
        }
        mHorizontalOffset += ndx
        offsetChildrenHorizontal(-ndx)
        // 检测到滑动，重新布局显示
        layoutChildren(recycler, state)

        return ndx
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    private fun isItemInFirstLine(index: Int): Boolean {
        val firstLineSize = mGroupSize / 2 + 1
        return (index < firstLineSize || index >= mGroupSize) && (index % mGroupSize < firstLineSize)
    }

    private fun getGroupSize(): Int {
        return ceil((itemCount / mGroupSize.toFloat()).toDouble()).toInt()
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    private fun getVerticalSpace(): Int {
        return height - paddingTop - paddingBottom
    }
}