package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
            String safeNumber = mPerf.getString("safeNumber", "");
            TextView tvSafeNumber = (TextView) findViewById(R.id.tv_safeNumber);
            tvSafeNumber.setText(safeNumber);

            boolean protect = mPerf.getBoolean("protect", false);
            ImageView ivProtect = (ImageView)findViewById(R.id.iv_protect);
            if(protect){
                ivProtect.setImageResource(R.drawable.lock);
            }else{
                ivProtect.setImageResource(R.drawable.unlock);
            }

        }else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }

    public void reEnter(View view){
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
