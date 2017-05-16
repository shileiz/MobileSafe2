package com.zsl.android.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zsl.android.mobilesafe.application.BaseApplication;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mPerf = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = mPerf.getBoolean(BaseApplication.PREF_KEY_PROTECTED, false);
        if(protect){
            String simSaved = mPerf.getString(BaseApplication.PREF_KEY_SIM_SERIAL, null);
            if(simSaved!=null){
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
                String simCurrent =tm.getSimSerialNumber();
                if(!simSaved.equals(simCurrent)){
                    String safeNum = mPerf.getString(BaseApplication.PREF_KEY_SAFE_NUMMBER, "");
                    if(!TextUtils.isEmpty(safeNum)){
                        SmsManager manager = SmsManager.getDefault();
                        manager.sendTextMessage(safeNum, null, "sim card changed!",null, null);
                    }
                }
            }
        }
    }
}

