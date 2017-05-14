package com.zsl.android.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mPerf = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = mPerf.getBoolean("protect", false);
        if(protect){
            String simSaved = mPerf.getString("sim", null);
            if(simSaved!=null){
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
                String simCurrent =tm.getSimSerialNumber();
                if(!simSaved.equals(simCurrent)){
                    //发报警短信
                }
            }
        }
    }
}

