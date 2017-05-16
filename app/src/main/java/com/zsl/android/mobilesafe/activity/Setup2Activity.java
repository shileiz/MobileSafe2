package com.zsl.android.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView sivSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sivSim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = mPerf.getString(BaseApplication.PREF_KEY_SIM_SERIAL, null);
        if (sim != null) {
            sivSim.setChecked(true);
        } else {
            sivSim.setChecked(false);
        }
    }

    @Override
    protected void showPrevious() {
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }

    @Override
    protected void showNext() {
        startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
    }

    public void next(View view) {
        showNext();
    }

    public void previous(View view) {
        showPrevious();
    }

    public void simClick(View view) {
        sivSim.toggleStatus();
        if (sivSim.isChecked()) {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String sim = tm.getSimSerialNumber();
            if (sim != null) {
                mPerf.edit().putString(BaseApplication.PREF_KEY_SIM_SERIAL, sim).apply();
            }else{
                Toast.makeText(this, "未插入sim卡，无法绑定", Toast.LENGTH_LONG).show();
                sivSim.setChecked(false);
            }
        } else {
            mPerf.edit().remove(BaseApplication.PREF_KEY_SIM_SERIAL).apply();
        }
    }
}
