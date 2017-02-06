package com.touchrom.gaoshouyou.util;

import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by lyy on 2015/12/2.
 * 简单的SnackBar模板
 */
public class S {

    public static int bgColor = 0x0cc6c6;

    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff2195f3;
    private static final int orange = 0xffffc107;


    /**
     * 简单的SnackBar样式
     *
     * @param view
     * @param info
     */
    public static void i(View view, CharSequence info) {
        Snackbar s = Snackbar.make(view, info, Snackbar.LENGTH_LONG);
//        s.getView().setBackgroundColor(bgColor);
        s.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public static void i(View view, CharSequence info, View.OnClickListener onClick) {
        Snackbar s = Snackbar.make(view, info, Snackbar.LENGTH_LONG);
//        s.getView().setBackgroundColor(bgColor);
        s.setAction("知道了", onClick).show();
    }

    /**
     * 可修改action字体的
     *
     * @param view
     * @param info
     * @param actionText
     */
    public static void i(View view, CharSequence info, CharSequence actionText) {
        Snackbar s = Snackbar.make(view, info, Snackbar.LENGTH_LONG);
//        s.getView().setBackgroundColor(bgColor);
        s.setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public static void i(View view, CharSequence info, CharSequence actionText, View.OnClickListener onClick) {
        Snackbar s = Snackbar.make(view, info, Snackbar.LENGTH_LONG);
//        s.getView().setBackgroundColor(bgColor);
        s.setAction(actionText, onClick).show();
    }

    private static Snackbar createSnackBar(View view, CharSequence info, @ColorInt int bg,
                                           CharSequence action, @ColorInt int actionColor,
                                           int during, View.OnClickListener onClick) {
        Snackbar s = Snackbar.make(view, info, during);
        s.getView().setBackgroundColor(bg);
        s.setAction(action, onClick).setActionTextColor(actionColor);
        return s;
    }

}
