package com.qiniu.niucube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiniu.niucube.R;

public class CustomOtherLoginView extends FrameLayout {
    public CustomOtherLoginView(@NonNull Context context) {
        this(context, null);
    }

    public CustomOtherLoginView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomOtherLoginView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_custom_other_login, this, true);
        findViewById(R.id.login_demo_weixin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了微信", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.login_demo_qq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击qq", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.login_demo_weibo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了微博", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
