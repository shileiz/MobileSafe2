package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;

public class SettingPositionActivity extends Activity {

    private ImageView ivDrag;
    private TextView tvBottom;
    private TextView tvTop;

    private SharedPreferences mPref;
    private long firstClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_position);

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        // 摆放图片位置
        int lastX = mPref.getInt(BaseApplication.PREF_KEY_ADDRESS_POSISTION_X, 0);
        int lastY = mPref.getInt(BaseApplication.PREF_KEY_ADDRESS_POSISTION_Y, 0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = lastX;
        layoutParams.topMargin = lastY;
        ivDrag.setLayoutParams(layoutParams);


        //获取窗口宽高
        WindowManager wm = getWindowManager();
        final int windowWidth = wm.getDefaultDisplay().getWidth();
        final int windowHeight = wm.getDefaultDisplay().getHeight();

        // 获取 ivDrag 宽高
        final int w = ivDrag.getWidth();
        final int h = ivDrag.getHeight();

        // 隐藏掉一个大框
        if (lastY > windowHeight / 2) {// 上边显示,下边隐藏
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        } else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }

        // 响应拖动事件
        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            int oldL;
            int oldR;
            int oldT;
            int oldB;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        oldL = ivDrag.getLeft();
                        oldR = ivDrag.getRight();
                        oldT = ivDrag.getTop();
                        oldB = ivDrag.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新左上右下距离
                        int l = oldL + dx;
                        int r = oldR + dx;
                        int t = oldT + dy;
                        int b = oldB + dy;

                        // 更新界面
                        if (l >= 0 && r <= windowWidth && t >= 0 && b <= windowHeight) {
                            ivDrag.layout(l, t, r, b);
                            if (t > windowHeight / 2) {// 上边显示,下边隐藏
                                tvTop.setVisibility(View.VISIBLE);
                                tvBottom.setVisibility(View.INVISIBLE);
                            } else {
                                tvTop.setVisibility(View.INVISIBLE);
                                tvBottom.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录坐标点
                        SharedPreferences.Editor edit = mPref.edit();
                        edit.putInt(BaseApplication.PREF_KEY_ADDRESS_POSISTION_X, ivDrag.getLeft());
                        edit.putInt(BaseApplication.PREF_KEY_ADDRESS_POSISTION_Y, ivDrag.getTop());
                        edit.apply();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        // 响应双击事件
        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstClickTime > 0) {// 发现之前点击过一次
                    if (System.currentTimeMillis() - firstClickTime < 500) {
                        ivDrag.layout(windowWidth / 2 - w / 2, ivDrag.getTop(), windowWidth / 2 + w / 2, ivDrag.getBottom());
                        Toast.makeText(SettingPositionActivity.this, "双击啦!", Toast.LENGTH_SHORT).show();
                        firstClickTime = 0;//重置时间, 重新开始
                        return;
                    }
                }
                firstClickTime = System.currentTimeMillis();
            }
        });
    }
}
