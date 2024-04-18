package com.room.layoutmanagerdemo.theme

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.room.layoutmanagerdemo.R
import com.room.layoutmanagerdemo.ui.SkinableImageView

class SkinFactory(val appCompatDelegate: AppCompatDelegate) : LayoutInflater.Factory2 {

    private val TAG = "SkinFactory"
    private val skinList: MutableList<SkinView> = mutableListOf()

    /**
     * Version of [.onCreateView]
     * that also supplies the parent that the view created view will be
     * placed in.
     *
     * @param parent The parent that the created view will be placed
     * in; *note that this may be null*.
     * @param name Tag name to be inflated.
     * @param context The context the view is being created in.
     * @param attrs Inflation attributes as specified in XML file.
     *
     * @return View Newly created view. Return null for the default
     * behavior.
     */
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        var newName: String? = null
        var view: View? = null
        when (name) {
            "ImageView" ->
                view = SkinableImageView(context, attrs)
        }
        if (view == null) {
            view = appCompatDelegate.createView(parent, name, context, attrs)
        }
        view?.let {
            collectSkinComponent(attrs, context, it)
        }
        return view
    }

    /**
     * Hook you can supply that is called when inflating from a LayoutInflater.
     * You can use this to customize the tag names available in your XML
     * layout files.
     *
     *
     *
     * Note that it is good practice to prefix these custom names with your
     * package (i.e., com.coolcompany.apps) to avoid conflicts with system
     * names.
     *
     * @param name Tag name to be inflated.
     * @param context The context the view is being created in.
     * @param attrs Inflation attributes as specified in XML file.
     *
     * @return View Newly created view. Return null for the default
     * behavior.
     */
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    /**
     * 收集能够进行换肤的控件
     */
    private fun collectSkinComponent(attrs: AttributeSet, context: Context, view: View) {
        //获取属性
        val skinAbleAttr = context.obtainStyledAttributes(attrs, R.styleable.Skinable, 0, 0)
        val isSupportSkin = skinAbleAttr.getBoolean(R.styleable.Skinable_supportSkin, false)
        if (isSupportSkin) {
            val attrsMap: MutableMap<String, String> = mutableMapOf()
            //收集起来
            for (index in 0 until attrs.attributeCount) {
                val name = attrs.getAttributeName(index)
                val value = attrs.getAttributeValue(index)
                if (value.startsWith("@")) {
                    // resourceId
                    val id: Int = Integer.parseInt(value.substring(1, value.length))
                    when (name) {
                        "textColor" -> {
                            Log.e(
                                TAG,
                                "collectSkinComponent textColor resource name:" + context.resources.getResourceName(id)
                            )
                        }

                        "src" -> {
                            Log.e(
                                TAG,
                                "collectSkinComponent src resource name:" + context.resources.getResourceName(id)
                            )
                        }
                    }
                }
                attrsMap[name] = value
            }
            val skinView = SkinView(view, attrsMap)
            skinList.add(skinView)
            if (view is TextView) {
                view.textColors
                view.setTextColor(context.getColor(R.color.textSkin))
            } else if (view is ImageView) {
                view.setImageDrawable(context.getDrawable(R.drawable.ball_bg))
            }
        }

        skinAbleAttr.recycle()
    }

    /**
     * 一键换肤
     */
    fun changedSkin(vararg skinType: SkinType) {
        Log.e("TAG", "skinList $skinList")
        skinList.forEach { skinView ->
            changedSkinInner(skinView, skinType)
        }
    }

    /**
     * 换肤的内部实现类
     */
    private fun changedSkinInner(skinView: SkinView, skinType: Array<out SkinType>) {
        skinType.forEach { type ->
            Log.e("TAG", "changedSkinInner $type")
            when (type) {
                is SkinType.BackgroundSkin -> {
                    skinView.view.setBackgroundColor(type.color)
                }

                is SkinType.BackgroundDrawableSkin -> {
                    skinView.view.setBackgroundResource(type.drawable)
                }

                is SkinType.TextStyleSkin -> {
                    if (skinView.view is TextView) {
                        //只有TextView可以换
                        skinView.view.typeface = type.textStyle
                    }
                }

                is SkinType.TextColorSkin -> {
                    if (skinView.view is TextView) {
                        //只有TextView可以换
                        skinView.view.setTextColor(type.color)
                    }
                }
            }
        }
    }
}

data class SkinView(val view: View, val attrsMap: MutableMap<String, String>)
