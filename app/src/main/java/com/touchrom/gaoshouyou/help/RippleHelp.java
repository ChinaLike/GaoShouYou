package com.touchrom.gaoshouyou.help;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.lyy.ui.group.MaterialRippleLayout;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lk on 2015/12/1.
 * 创建水波纹选择器
 */
public class RippleHelp {

    public static void createRipple(@NonNull Context context, View view) {
        createRipple(context, view, context.getResources().getColor(R.color.rippleDefaultColor));
    }

    public static void createRipple(@NonNull Context context, View view, @ColorInt int rippleColor) {
        int color = context.getResources().getColor(R.color.skin_background_color);
        MaterialRippleLayout.on(view)
                .rippleColor(rippleColor)
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleDuration(200)
                .rippleBackground(color)
                .create();
    }
}
