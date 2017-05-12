package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zsl.android.mobilesafe.R;

public class LostfindActivity extends Activity {

    private SharedPreferences mPerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfind);
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        boolean alreadySetup = mPerf.getBoolean("alreadySetup", false);
        if(alreadySetup){

        }else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }
}
