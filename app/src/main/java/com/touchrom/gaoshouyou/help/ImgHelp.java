package com.touchrom.gaoshouyou.help;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2015/12/10.
 * 图片帮助类
 */
public class ImgHelp {

    public static AnimationDrawable createLoadingAnim(Context context) {
        AnimationDrawable ad = new AnimationDrawable();
        ad.addFrame(context.getResources().getDrawable(R.drawable.icon_refresh_left), 200);
        ad.addFrame(context.getResources().getDrawable(R.drawable.icon_refresh_center), 200);
        ad.addFrame(context.getResources().getDrawable(R.drawable.icon_refresh_right), 200);
        ad.setOneShot(false);
        return ad;
    }

    /**
     * 设置图片
     *
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public static void setImg(Context context, String imgUrl, ImageView imageView) {
        setImg(context, imgUrl, R.mipmap.default_icon, imageView);
    }

    /**
     * 设置图片
     *
     * @param context
     * @param imgUrl
     * @param drawableRes
     * @param imageView
     */
    public static void setImg(Context context, String imgUrl, @DrawableRes int drawableRes, ImageView imageView) {
        Glide.with(context).load(imgUrl)
                .placeholder(drawableRes)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
//                .fitCenter()
                .error(drawableRes)
                .into(imageView);
    }

}
