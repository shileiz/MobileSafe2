package com.zsl.android.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zsl.android.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    protected void showPrevious() {

    }

    @Override
    protected void showNext() {
        startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
    }

    public void next(View view){
        showNext();
    }
}
