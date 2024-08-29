package com.room.layoutmanagerdemo

import android.app.Application
import android.view.LayoutInflater
import com.room.layoutmanagerdemo.databinding.FragmentDemoMenusBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ArrayBlockingQueue

class AppApplication : Application() {

    companion object {
        lateinit var instance: AppApplication
        val demoMenusCache: ArrayBlockingQueue<FragmentDemoMenusBinding> = ArrayBlockingQueue(15)
        fun preCreateViewCache() {
            CoroutineScope(Dispatchers.IO).launch {
                while (demoMenusCache.remainingCapacity() > 0) {
                    val view = FragmentDemoMenusBinding.inflate(LayoutInflater.from(instance))
                    demoMenusCache.offer(view)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
//        Debug.startMethodTracingSampling("${cacheDir}/app.trace", 10 * 1024 * 1024, 1000)
//        AsyncInflateUtil.startTask(this)
        instance = this
        preCreateViewCache()
    }
}