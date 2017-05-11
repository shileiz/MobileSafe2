package com.zsl.android.mobilesafe.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate = mPref.getBoolean("AutoUpdate", true);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        sivUpdate.setChecked(autoUpdate);
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sivUpdate.toggleStatus();
                mPref.edit().putBoolean("AutoUpdate", sivUpdate.isChecked()).commit();
            }
        });
    }
}
