package com.gucheng.clockoff;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

/**
 * Created by liuwei on 2018/1/13.
 */

public class ClockApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
    }

}
