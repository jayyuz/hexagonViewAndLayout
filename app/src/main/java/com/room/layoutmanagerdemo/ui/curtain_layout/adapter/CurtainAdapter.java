package com.room.layoutmanagerdemo.ui.curtain_layout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


public abstract class CurtainAdapter {

    //返回总共子View的数量
    public abstract int getItemCount();

    //根据索引创建不同的布局类型，如果都是一样的布局则不需要重写
    public int getItemViewType(int position) {
        return 0;
    }

    //根据类型创建对应的View布局
    public abstract View onCreateItemView(@NonNull Context context, @NonNull ViewGroup parent, int itemType);

    //可以根据类型或索引绑定数据
    public abstract void onBindItemView(@NonNull View itemView, int itemType, int position);

}
