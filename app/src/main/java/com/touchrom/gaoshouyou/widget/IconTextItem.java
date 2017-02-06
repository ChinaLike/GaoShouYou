package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.touchrom.gaoshouyou.R;

/**
 * Created by lyy on 2016/2/26.
 */
public class IconTextItem extends RelativeLayout {

    private ImageView mImg;
    private TextView mText;

    public IconTextItem(Context context) {
        this(context, null);
    }

    public IconTextItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_icon_text_item, this, true);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextItem, defStyleAttr, 0);
        Drawable drawable = a.getDrawable(R.styleable.IconTextItem_it_img);
        CharSequence text = a.getString(R.styleable.IconTextItem_it_text);
        a.recycle();
        if (drawable == null) {
            drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        }
        mImg = (ImageView) findViewById(R.id.img);
        mText = (TextView) findViewById(R.id.text);
        mImg.setImageDrawable(drawable);
        mText.setText(text);
    }

}
