package com.room.layoutmanagerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.room.layoutmanagerdemo.databinding.ActivityMainBinding
import com.room.layoutmanagerdemo.theme.SkinFactory


class MainActivity : AppCompatActivity() {

    private lateinit var skinFactory: SkinFactory
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        skinFactory = SkinFactory(delegate)
        val layoutInflater = layoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.post {
            binding.root.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {

                override fun onPreDraw(): Boolean {
                    binding.root.viewTreeObserver.removeOnPreDrawListener(this)
//                    Debug.stopMethodTracing()
                    return true
                }
            })
        }

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAnchorView(R.id.fab)
//                    .setAction("Action", null).show()
//            Debug.stopMethodTracing()
//        }

    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun resetmFactorySet(instance: LayoutInflater) {
        val mFactorySetField = LayoutInflater::class.java.getDeclaredField("mFactorySet")
        mFactorySetField.isAccessible = true
        mFactorySetField.set(instance, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun getToolBar(): MaterialToolbar {
        return binding.toolbar
    }
}