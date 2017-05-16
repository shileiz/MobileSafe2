package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;

public class Setup3Activity extends BaseSetupActivity {

    private EditText etSafeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etSafeNumber = (EditText) findViewById(R.id.et_safenumber);
        String safeNumber = mPerf.getString(BaseApplication.PREF_KEY_SAFE_NUMMBER,"");
        etSafeNumber.setText(safeNumber);
    }

    public void chooseContact(View view) {
        startActivityForResult(new Intent(this, ContactActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = data.getStringExtra("phone");
        if (resultCode == Activity.RESULT_OK) {
            if (!TextUtils.isEmpty(phone))
                etSafeNumber.setText(phone.replaceAll("-", "").replace(" ", ""));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void showPrevious() {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.to_right_in, R.anim.to_right_out);
    }

    @Override
    protected void showNext() {
        String safeNum = etSafeNumber.getText().toString().trim();
        if (TextUtils.isEmpty(safeNum)) {
            Toast.makeText(Setup3Activity.this, "请输入号码或选择联系人", Toast.LENGTH_SHORT).show();
        } else {
            mPerf.edit().putString(BaseApplication.PREF_KEY_SAFE_NUMMBER, safeNum).apply();
            startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
            finish();
            overridePendingTransition(R.anim.to_left_in, R.anim.to_left_out);
        }
    }

    public void next(View view) {
        showNext();
    }

    public void previous(View view) {
        showPrevious();
    }
}
