package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;

public class Setup3Activity extends Activity {

    private Button btnNext;
    private Button btnPrevious;
    private SharedPreferences mPerf;
    private EditText etSafeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        etSafeNumber = (EditText) findViewById(R.id.et_safenumber);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnPrevious = (Button) findViewById(R.id.btn_previous);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etSafeNumber.getText().toString())){
                    Toast.makeText(Setup3Activity.this, "请输入号码或选择联系人", Toast.LENGTH_SHORT).show();
                }else {
                    mPerf.edit().putString("safeNumber", etSafeNumber.getText().toString()).apply();
                    startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
