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
 * @description 横向排列的瀑布流
 */
public class HorizontalLayoutManager implements ILayoutManager {

    @SuppressLint("UseSparseArrays")
    @Override
    public int[] performMeasure(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedHeight) {

        int childCount = viewGroup.getChildCount();
        int curCount = 1;
        int totalControlHeight = 0;
        int totalControlWidth = 0;
        int curRow = 0;
        SparseArray<Integer> rowTotalWidth = new SparseArray<>();  //每一行的总宽度

        //开始遍历
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);

            int row = curCount / rowCount;    //当前子View是第几行

            //已经测量过了，直接取宽高
            int width = childView.getMeasuredWidth();

            if (row == curRow) {
                //当前行
                totalControlWidth += width + horizontalSpacing;

            } else {
                //换行了
                totalControlWidth = width + horizontalSpacing;
            }

            rowTotalWidth.put(row, totalControlWidth);

            //赋值
            curCount++;
            curRow = row;
        }

        //循环结束之后开始计算真正的宽高
        totalControlHeight = (rowCount * (fixedHeight + verticalSpacing)) - verticalSpacing +
                viewGroup.getPaddingTop() + viewGroup.getPaddingBottom();

        List<Integer> widthList = new ArrayList<>();
        for (int i = 0; i < rowTotalWidth.size(); i++) {
            Integer width = rowTotalWidth.get(i);
            widthList.add(width);
        }
        totalControlWidth = Collections.max(widthList);

        rowTotalWidth.clear();
        rowTotalWidth = null;

        return new int[]{totalControlWidth - horizontalSpacing, totalControlHeight - verticalSpacing};
    }

    @Override
    public void performLayout(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedHeight) {
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
            int width = childView.getMeasuredWidth();

            childView.layout(layoutChildViewCurX, layoutChildViewCurY, layoutChildViewCurX + width, layoutChildViewCurY + fixedHeight);

            if (row == curRow) {
                //同一行
                layoutChildViewCurX += width + horizontalSpacing;

            } else {
                //换行了
                layoutChildViewCurX = childView.getPaddingLeft();
                layoutChildViewCurY += fixedHeight + verticalSpacing;
            }

            //赋值
            curCount++;
            curRow = row;

        }
    }

    @Override
    public int getLayoutDirection() {
        return DIRECTION_HORIZONTAL;
    }
}
