package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.dao.AddressDao;

public class AddressActivity extends Activity {

    private EditText etNumber;
    private Button btnQuery;
    private TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        etNumber = (EditText) findViewById(R.id.et_number);
        btnQuery = (Button) findViewById(R.id.btn_query);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etNumber.getText().toString().trim();
                String address = queryAddress(number);
                tvAddress.setText(address);
            }
        });
    }

    private String queryAddress(String number) {
        String address = "";
        if (number.matches("^1[3-8]\\d{5,9}$")) {
            address = AddressDao.getAddress(this, number.substring(0,7));
            if(TextUtils.isEmpty(address)){
                address = "未知号码";
            }
        }else{
            Toast.makeText(this,"仅支持手机号",Toast.LENGTH_SHORT).show();
        }
        return address;
    }
}
