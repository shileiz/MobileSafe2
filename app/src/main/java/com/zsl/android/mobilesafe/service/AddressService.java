package com.zsl.android.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.dao.AddressDao;

public class AddressService extends Service {

    private TelephonyManager tm;
    private WindowManager mWM;
    private View view;
    private MyListener listener;
    private SharedPreferences mPref;

    @Override
    public void onCreate() {
        super.onCreate();

        // 来电监听、去电结束监听，用 TelephonyManager
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        // 去电监听，用 BroadcastReceiver
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String number = getResultData();
                String address = queryAddress(number);
                showFloatWindow(address);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showFloatWindow(String text) {
        mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        view = View.inflate(this, R.layout.toast_address, null);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        int styleIndex = mPref.getInt(BaseApplication.PREF_KEY_ADDRESS_STYLE, 0);
        int[] Styles = new int[]{R.drawable.call_locate_white, R.drawable.call_locate_orange,
                R.drawable.call_locate_blue, R.drawable.call_locate_gray, R.drawable.call_locate_green};
        view.setBackgroundResource(Styles[styleIndex]);
        ((TextView)view.findViewById(R.id.tv_number)).setText(text);
        mWM.addView(view, params);
    }

    private String queryAddress(String number) {
        String address;
        if (number.matches("^1[3-8]\\d{5,9}$")) {
            address = AddressDao.getAddress(this, number.substring(0, 7));
            if (TextUtils.isEmpty(address)) {
                address = "未知地区号码";
            }
        } else {
            address = "未知地区号码";
        }
        return address;
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = queryAddress(incomingNumber);
                    showFloatWindow(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mWM != null && view != null) {
                        mWM.removeView(view);
                        view = null;
                    }
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
