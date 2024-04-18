package com.room.layoutmanagerdemo.ui.curtain_layout.layoutmanager;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @auther Newki
 * @date 2023/1/16
 * @description 垂直排列的瀑布流
 */
public class VerticalLayoutManager implements ILayoutManager {

    @SuppressLint("UseSparseArrays")
    @Override
    public int[] performMeasure(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedWidth) {

        int childCount = viewGroup.getChildCount();
        int curPosition = 0;
        int totalControlHeight = 0;
        int totalControlWidth = 0;
        SparseArray<List<Integer>> columnAllHeight = new SparseArray<>(); //每一列的全部高度

        //开始遍历
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);

            int row = curPosition / rowCount;    //当前子View是第几行
            int column = curPosition % rowCount;    //当前子View是第几列

            //已经测量过了，直接取宽高
            int height = childView.getMeasuredHeight();

            List<Integer> integers = columnAllHeight.get(column);
            if (integers == null || integers.isEmpty()) {
                integers = new ArrayList<>();
            }
            integers.add(height + verticalSpacing);
            columnAllHeight.put(column, integers);

            //赋值
            curPosition++;
        }

        //循环结束之后开始计算真正的宽高
        totalControlWidth = (rowCount *
                (fixedWidth + horizontalSpacing) + viewGroup.getPaddingLeft() + viewGroup.getPaddingRight());

        List<Integer> totalHeights = new ArrayList<>();
        for (int i = 0; i < columnAllHeight.size(); i++) {
            List<Integer> heights = columnAllHeight.get(i);
            int totalHeight = 0;
            for (int j = 0; j < heights.size(); j++) {
                totalHeight += heights.get(j);
            }
            totalHeights.add(totalHeight);
        }
        totalControlHeight = Collections.max(totalHeights);

        columnAllHeight.clear();
        columnAllHeight = null;

        return new int[]{totalControlWidth - horizontalSpacing, totalControlHeight - verticalSpacing};
    }

    @Override
    public void performLayout(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedWidth) {

        int childCount = viewGroup.getChildCount();

        int curCount = 1;
        int layoutChildViewCurX = viewGroup.getPaddingLeft();
        int layoutChildViewCurY = viewGroup.getPaddingTop();
        int curRow = 0;

        //开始遍历
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);

            int row = curCount / rowCount;    //当前子View是第几行

            //每一个子View宽度
            int height = childView.getMeasuredHeight();

            if (i >= rowCount) {
                //从头上的ItemView拿到高度设置为Y
                View headView = viewGroup.getChildAt(i - rowCount);
                layoutChildViewCurY = headView.getBottom() + verticalSpacing;
            }

            childView.layout(layoutChildViewCurX, layoutChildViewCurY, layoutChildViewCurX + fixedWidth, layoutChildViewCurY + height);

            if (row == curRow) {
                //同一行
                layoutChildViewCurX += fixedWidth + horizontalSpacing;

            } else {
                //换行了
                layoutChildViewCurX = childView.getPaddingLeft();
            }

            //赋值
            curCount++;
            curRow = row;

        }
    }

    @Override
    public int getLayoutDirection() {
        return DIRECTION_VERITICAL;
    }

}
