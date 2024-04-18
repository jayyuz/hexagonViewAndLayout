package com.room.layoutmanagerdemo.ui.curtain_layout.layoutmanager;

import android.view.ViewGroup;


/**
 * @auther Newki
 * @date 2023/1/16
 * @description 接口限制
 */
public interface ILayoutManager {

    public static final int DIRECTION_VERITICAL = 0;
    public static final int DIRECTION_HORIZONTAL = 1;

    public abstract int[] performMeasure(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedValue);

    public abstract void performLayout(ViewGroup viewGroup, int rowCount, int horizontalSpacing, int verticalSpacing, int fixedValue);

    public abstract int getLayoutDirection();

}
