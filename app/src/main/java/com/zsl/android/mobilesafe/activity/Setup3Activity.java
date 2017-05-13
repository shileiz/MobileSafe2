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

    private SharedPreferences mPerf;
    private EditText etSafeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        etSafeNumber = (EditText) findViewById(R.id.et_safenumber);
    }

    public void next(View view){
        if(TextUtils.isEmpty(etSafeNumber.getText().toString())){
            Toast.makeText(Setup3Activity.this, "请输入号码或选择联系人", Toast.LENGTH_SHORT).show();
        }else {
            mPerf.edit().putString("safeNumber", etSafeNumber.getText().toString()).apply();
            startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
            finish();
            overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
        }
    }

    public void previous(View view){
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }
}
