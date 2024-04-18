package com.room.layoutmanagerdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.room.layoutmanagerdemo.theme.SkinFactory
import com.room.layoutmanagerdemo.theme.SkinType


class ChangeThemeFragment : Fragment() {

    private lateinit var skinFactory: SkinFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skinFactory = SkinFactory((context as AppCompatActivity).delegate)
    }

    private fun changedSkin(vararg skinType: SkinType) {
        Log.e("TAG", "changedSkin")
        skinFactory.changedSkin(*skinType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val cloneInflater: LayoutInflater = LayoutInflater.from(context).cloneInContext(context)
        cloneInflater.factory2 = skinFactory
        return cloneInflater.inflate(R.layout.fragment_change_theme, container, false)
    }
}