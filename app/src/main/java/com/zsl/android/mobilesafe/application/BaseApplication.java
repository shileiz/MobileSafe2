package com.zsl.android.mobilesafe.application;

import android.app.Application;
import org.xutils.x;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 xutils
        x.Ext.init(this);
    }

}
