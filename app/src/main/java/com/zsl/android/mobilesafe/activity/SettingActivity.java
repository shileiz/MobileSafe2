package com.zsl.android.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.service.AddressService;
import com.zsl.android.mobilesafe.view.SettingClickView;
import com.zsl.android.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SharedPreferences mPref;
    private SettingClickView scvAddressStyle;

    private String[] mAddressStyle = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
    private int mStyleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateItem();
        initAddressItem();
        initAddressStyleItem();
    }

    private void initUpdateItem() {
        boolean autoUpdate = mPref.getBoolean("AutoUpdate", true);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        sivUpdate.setChecked(autoUpdate);
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sivUpdate.toggleStatus();
                mPref.edit().putBoolean("AutoUpdate", sivUpdate.isChecked()).commit();
            }
        });
    }

    private void initAddressItem() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sivAddress.toggleStatus();
                if (sivAddress.isChecked()) {
                    startService(new Intent(SettingActivity.this, AddressService.class));
                } else {
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                }
            }
        });
    }

    private void initAddressStyleItem() {
        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
        mStyleIndex = mPref.getInt(BaseApplication.PREF_KEY_ADDRESS_STYLE, 0);
        scvAddressStyle.setSubTitle(mAddressStyle[mStyleIndex]);
        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressStyleDialog();
            }
        });
    }

    private void showAddressStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(mAddressStyle, mStyleIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPref.edit().putInt(BaseApplication.PREF_KEY_ADDRESS_STYLE, which).apply();
                        dialog.dismiss();
                        scvAddressStyle.setSubTitle(mAddressStyle[which]);
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
