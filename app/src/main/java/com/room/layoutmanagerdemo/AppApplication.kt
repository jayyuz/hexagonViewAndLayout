package com.room.layoutmanagerdemo

import android.app.Application
import com.room.layoutmanagerdemo.preload.AsyncInflateUtil

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        Debug.startMethodTracing(getDataDir().getPath() + "/app")
//        mainLooper.queue.addIdleHandler {
//            true
//        }
        AsyncInflateUtil.startTask(this)
    }
}