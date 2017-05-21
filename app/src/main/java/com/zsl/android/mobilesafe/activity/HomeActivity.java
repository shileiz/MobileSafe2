package com.zsl.android.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.utils.MD5Utils;

import java.util.Set;

public class HomeActivity extends Activity {

    // 组件
    private GridView gvHome;

    // 其他
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    SharedPreferences mPerf;

    private int[] mPics = new int[]{R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};

    private HomeAdapter homeAdapter = new HomeAdapter();

    private class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
            }
            ImageView ivItem = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvItem = (TextView) convertView.findViewById(R.id.tv_name);
            ivItem.setImageResource(mPics[position]);
            tvItem.setText(mItems[position]);
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(homeAdapter);
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showPasswordDialog();
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this, AdvanceActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }

    private void showPasswordDialog() {
        String savedPassword = mPerf.getString(BaseApplication.PREF_KEY_PASSWORD, null);
        if (savedPassword != null) {
            showInputPasswordDialog();
        } else {
            showSetPasswordDialog();
        }
    }

    private void showInputPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(password))
                    Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                else {
                    String savedPassword = mPerf.getString(BaseApplication.PREF_KEY_PASSWORD, null);
                    password = MD5Utils.encode(password);
                    if (password.equals(savedPassword)) {
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostfindActivity.class));
                    } else Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }


    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword))
                    Toast.makeText(HomeActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                else {
                    if (password.equals(confirmPassword)) {
                        password = MD5Utils.encode(password);
                        mPerf.edit().putString(BaseApplication.PREF_KEY_PASSWORD, password).apply();
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostfindActivity.class));
                    } else Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }
}
