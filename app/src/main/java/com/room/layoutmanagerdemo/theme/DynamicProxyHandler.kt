package com.room.layoutmanagerdemo.theme

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class DynamicProxyHandler(private val target: Any) : InvocationHandler {
    private final val TAG = "DynamicProxyHandler"

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        // 你可以在这里添加额外的逻辑
//        System.out.println("Proxy: Before calling " + method.getName());
        //        System.out.println("Proxy: After calling " + method.getName());
        Log.e(TAG, "invoke: ${method.name}")
        return method.invoke(target, *args)
    }
}