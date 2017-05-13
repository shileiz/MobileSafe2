package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.view.SettingItemView;

import java.util.Set;

public class Setup4Activity extends Activity {

    private SharedPreferences mPerf;
    private SettingItemView sivTurnOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        sivTurnOn = (SettingItemView)findViewById(R.id.siv_turn_on);

        Button btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setCompoundDrawables(null, null, null, null);

    }

    public void next(View view){
        mPerf.edit().putBoolean("alreadySetup", true).apply();
        startActivity(new Intent(Setup4Activity.this, HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
    }

    public void previous(View view){
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }

    public  void turnOn(View view){
        sivTurnOn.toggleStatus();
    }
}
