package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zsl.android.mobilesafe.R;

public class AdvanceActivity extends Activity {

    private TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        tvAddress = (TextView)findViewById(R.id.tv_address);
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvanceActivity.this, AddressActivity.class));
            }
        });
    }
}
