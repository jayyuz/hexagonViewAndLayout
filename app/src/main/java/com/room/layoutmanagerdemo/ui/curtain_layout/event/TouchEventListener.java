package com.room.layoutmanagerdemo.ui.curtain_layout.event;

/**
 * 事件的监听，缩放与移动
 */
public interface TouchEventListener {
    int MIN_SCALE = -1;
    int FREE_SCALE = 0;
    int MAX_SCALE = 1;

    void onScaling(int state, int percent);
}
