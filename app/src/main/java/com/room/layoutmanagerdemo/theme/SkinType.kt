package com.room.layoutmanagerdemo.theme

import android.graphics.Typeface

sealed class SkinType {
    /**
     * 更换背景颜色
     * @param color 背景颜色
     */
    class BackgroundSkin(val color: Int) : SkinType()

    /**
     * 更换背景图片
     * @param drawable 背景图片资源id
     */
    class BackgroundDrawableSkin(val drawable: Int) : SkinType()

    /**
     * 更换字体颜色
     * @param color 字体颜色
     * NOTE 这个只能TextView才能是用
     */
    class TextColorSkin(val color: Int) : SkinType()

    /**
     * 更换字体类型
     * @param textStyle 字体型号
     * NOTE 这个只能TextView才能是用
     */
    class TextStyleSkin(val textStyle: Typeface) : SkinType()
}