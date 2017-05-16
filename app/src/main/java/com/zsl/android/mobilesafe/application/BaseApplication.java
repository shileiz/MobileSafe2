package com.zsl.android.mobilesafe.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class BaseApplication extends Application {

    public static final String PREF_KEY_SAFE_ALREADY_SETUP = "alreadySetup";
    public static final String PREF_KEY_AUTO_UPDATE = "atuoUpdate";
    public static final String PREF_KEY_SAFE_NUMMBER = "safeNumber";
    public static final String PREF_KEY_SIM_SERIAL = "sim";
    public static final String PREF_KEY_PROTECTED = "protected";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_LOCATION = "location";

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 xutils
        x.Ext.init(this);
    }

}
