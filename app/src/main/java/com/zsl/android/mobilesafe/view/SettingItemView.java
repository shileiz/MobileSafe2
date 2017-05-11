package com.zsl.android.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsl.android.mobilesafe.R;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private String mSubtitleOn;
    private String mSubtitleOff;
    private String mTitle;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private CheckBox cbStatus;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitle = attrs.getAttributeValue(NAMESPACE, "title");
        mSubtitleOn = attrs.getAttributeValue(NAMESPACE, "subtitle_on");
        mSubtitleOff = attrs.getAttributeValue(NAMESPACE, "subtitle_off");
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        cbStatus = (CheckBox) findViewById(R.id.cb);
        tvTitle.setText(mTitle);
        if (cbStatus.isChecked())
            tvSubtitle.setText(mSubtitleOn);
        else
            tvSubtitle.setText(mSubtitleOff);
    }

    public void toggleStatus(){
        cbStatus.setChecked(!cbStatus.isChecked());
        if (cbStatus.isChecked())
            tvSubtitle.setText(mSubtitleOn);
        else
            tvSubtitle.setText(mSubtitleOff);
    }

    public void setChecked(boolean autoUpdate) {
        cbStatus.setChecked(autoUpdate);
        if (cbStatus.isChecked())
            tvSubtitle.setText(mSubtitleOn);
        else
            tvSubtitle.setText(mSubtitleOff);
    }

    public boolean isChecked(){
        return cbStatus.isChecked();
    }
}
