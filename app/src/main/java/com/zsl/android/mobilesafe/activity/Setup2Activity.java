package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

    private SettingItemView sivSim;
    private SharedPreferences mPerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        sivSim = (SettingItemView) findViewById(R.id.siv_sim);

    }

    public void next(View view){
        if (sivSim.isChecked()) {
            mPerf.edit().putBoolean("sim", true).apply();
        } else {
            mPerf.edit().putBoolean("sim", false).apply();
        }
        startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
    }

    public void previous(View view){
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();

        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }

    public void simClick(View view){
        sivSim.toggleStatus();
    }
}
