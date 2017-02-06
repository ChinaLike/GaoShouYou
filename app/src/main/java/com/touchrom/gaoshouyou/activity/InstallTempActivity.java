package com.touchrom.gaoshouyou.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arialyy.frame.util.AndroidUtils;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.config.Constance;

import java.io.File;

/**
 * Created by lyy on 2015/11/13.
 * 麻痹，谷歌就是喜欢坑开发者，使用MVVM框架安装界面就不能正常显示，妈的
 */
public class InstallTempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_temp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String path = getIntent().getStringExtra(Constance.KEY.STRING);
        File apk = new File(path);
        if (apk.exists()) {
            AndroidUtils.install(this, new File(path));
        }
        finish();
    }
}
