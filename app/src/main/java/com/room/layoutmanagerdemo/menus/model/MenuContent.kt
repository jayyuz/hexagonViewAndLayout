package com.room.layoutmanagerdemo.menus.model

import androidx.annotation.IdRes
import com.room.layoutmanagerdemo.R


object MenuContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<MenuItem> = ArrayList()


    init {
        addItem(MenuItem("First", R.id.action_DemoMenusFragment_to_FirstFragment))
        addItem(MenuItem("Second", R.id.action_DemoMenusFragment_to_SecondFragment))
        addItem(MenuItem("CustomNestedView", R.id.action_DemoMenusFragment_to_ThirdFragment))
        addItem(MenuItem("EdgeEffect", R.id.action_DemoMenusFragment_to_EdgeEffectFragment))
        addItem(MenuItem("LinearLayout", R.id.action_DemoMenusFragment_to_LinearLayoutFragment))
        addItem(MenuItem("ChangeThemeFragment", R.id.action_DemoMenusFragment_to_ChangeThemeFragment))
        addItem(MenuItem("CurtainFragment", R.id.action_DemoMenusFragment_to_CurtainFragment))
    }

    private fun addItem(item: MenuItem) {
        ITEMS.add(item)
    }


    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class MenuItem(val content: String, @IdRes val resId: Int) {
        override fun toString(): String = content
    }
}