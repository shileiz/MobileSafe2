package com.zsl.android.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.view.SettingItemView;


public class Setup4Activity extends BaseSetupActivity {

    private SettingItemView sivTurnOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        boolean protect = mPerf.getBoolean("protect", false);

        sivTurnOn = (SettingItemView) findViewById(R.id.siv_turn_on);
        if (protect) {
            sivTurnOn.setChecked(true);
        }else {
            sivTurnOn.setChecked(false);
        }
        Button btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setCompoundDrawables(null, null, null, null);

    }

    @Override
    protected void showPrevious() {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }

    @Override
    protected void showNext() {
        mPerf.edit().putBoolean("alreadySetup", true).apply();
        startActivity(new Intent(Setup4Activity.this, HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
    }

    public void next(View view) {
        showNext();
    }

    public void previous(View view) {
        showPrevious();
    }

    public void turnOn(View view) {
        sivTurnOn.toggleStatus();
        if(sivTurnOn.isChecked()){
            mPerf.edit().putBoolean("protect", true).apply();
        }else {
            mPerf.edit().putBoolean("protect", false).apply();
        }
    }
}
