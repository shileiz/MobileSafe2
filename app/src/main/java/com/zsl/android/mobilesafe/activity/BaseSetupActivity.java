package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;


public abstract class BaseSetupActivity extends Activity {

    protected SharedPreferences mPerf;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPerf = getSharedPreferences("config", MODE_PRIVATE);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPrevious();
                    return true;
                }
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNext();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showPrevious() ;
    protected abstract void showNext();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
