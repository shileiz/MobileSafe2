package com.zsl.android.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.service.LocationService;
import com.zsl.android.mobilesafe.utils.PermissionUtils;

import static android.content.Context.LOCATION_SERVICE;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] objects = (Object[]) bundle.get("pdus");
        for (Object ob : objects) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) ob);
            String messageBody = sms.getMessageBody();
            if ("#*alarm*#".equals(messageBody)) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.start();
                abortBroadcast();
            } else if ("#*location*#".equals(messageBody)) {
                context.startService(new Intent(context, LocationService.class));
                SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String location = sp.getString(BaseApplication.PREF_KEY_LOCATION, "getting location...");
                System.out.println("location:" + location);
                abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
            } else if("#*wipedata*#".equals(messageBody)){
                // 擦除数据
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(messageBody)){
                abortBroadcast();
            }
        }
    }


}
