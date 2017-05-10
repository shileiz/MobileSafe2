package com.zsl.android.mobilesafe.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;

public class HomeActivity extends Activity {

    // 组件
    private GridView gvHome;

    // 其他
    private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

    private int[] mPics = new int[] { R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings };

    private HomeAdapter homeAdapter = new HomeAdapter();

    class HomeAdapter extends BaseAdapter{

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
            if(null == convertView){
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
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(homeAdapter);
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, "1.0"+mItems[position],Toast.LENGTH_SHORT).show();
            }
        });
    }

}
