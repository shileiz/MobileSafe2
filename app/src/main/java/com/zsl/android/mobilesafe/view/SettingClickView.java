package com.zsl.android.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsl.android.mobilesafe.R;


public class SettingClickView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private String mSubtitle;
    private String mTitle;
    private TextView tvTitle;
    private TextView tvSubtitle;

    public SettingClickView(Context context) {
        super(context);
        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitle = attrs.getAttributeValue(NAMESPACE, "title");
        mSubtitle = attrs.getAttributeValue(NAMESPACE, "subtitle");
        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.setting_click_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        tvTitle.setText(mTitle);
        tvSubtitle.setText(mSubtitle);
    }

    public void setSubTitle(String subTitle){
        tvSubtitle.setText(subTitle);
    }
}
