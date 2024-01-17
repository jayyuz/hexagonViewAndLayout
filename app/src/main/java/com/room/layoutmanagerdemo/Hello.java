package com.room.layoutmanagerdemo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Hello {

    void start() {
        EventBus.getDefault().register(this);
    }

    void end() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 2)
    public void test(EventMessage eventMessage) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test2(EventMessage eventMessage) {

    }
}
