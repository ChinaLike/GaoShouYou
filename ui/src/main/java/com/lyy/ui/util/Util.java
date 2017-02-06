package com.lyy.ui.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lyy on 2015/11/23.
 */
public class Util {
    /**
     * 获取类里面的所在字段
     */
    public static Field[] getFields(Class clazz) {
        Field[] fields = null;
        fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz != null) {
                fields = getFields(superClazz);
            }
        }
        return fields;
    }

    /**
     * 获取类里面的指定对象，如果该类没有则从父类查询
     */
    public static Field getField(Class clazz, String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                field = clazz.getField(name);
            } catch (NoSuchFieldException e1) {
                if (clazz.getSuperclass() == null) {
                    return field;
                } else {
                    field = getField(clazz.getSuperclass(), name);
                }
            }
        }
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
    }

    /**
     * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
     *
     * @param clazz      目标类
     * @param methodName 方法名
     * @param params     方法参数类型数组
     * @return 方法对象
     */
    public static Method getMethod(Class clazz, String methodName, final Class... params) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, params);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName, params);
                }
            }
        }
        if (method != null) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * 获取对象名
     *
     * @param obj 对象
     * @return 对象名
     */
    public static String getClassName(Object obj) {
        String arrays[] = obj.getClass().getName().split("\\.");
        return arrays[arrays.length - 1];
    }

    /**
     * 创建圆角shape
     *
     * @param radiusWidth 圆角半径
     */
    public static GradientDrawable createRoundShape(int color, int radiusWidth) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(dp2px(radiusWidth));
        return gd;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
//    private static final float Resources.getSystem().getDisplayMetrics().

    /**
     * 另外一种dp转PX方法
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
