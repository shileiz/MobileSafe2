package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

    private Button btnNext;
    private Button btnPrevious;
    private SettingItemView sivSim;
    private SharedPreferences mPerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        sivSim = (SettingItemView) findViewById(R.id.siv_sim);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivSim.isChecked()) {
                    mPerf.edit().putBoolean("sim", true).apply();
                } else {
                    mPerf.edit().putBoolean("sim", false).apply();
                }
                startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sivSim.toggleStatus();
            }
        });
    }
}
