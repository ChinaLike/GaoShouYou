package com.lyy.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyy.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/3/4.
 * 回复TextView
 */
public class ReplyText extends LinearLayout {
    List<ReplyEntity> mData = new ArrayList<>();
    private OnReplyClickListener mLinkListener;
    private int mTextColor, mOtherColor;

    public interface OnReplyClickListener {
        public void onReplyClick(View parent, int id);

        public void onReplyContentClick(View parent, ReplyEntity entity);
    }

    public ReplyText(Context context) {
        this(context, null);
    }

    public ReplyText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RichText, defStyleAttr, 0);
        mTextColor = a.getColor(R.styleable.ReplyText_rt_textColor, Color.parseColor("#2A2A2A"));
        mOtherColor = a.getColor(R.styleable.ReplyText_rt_otherTxColor, Color.parseColor("#b5b5b5"));
        a.recycle();
    }

    public void setOnReplyClickListener(@NonNull OnReplyClickListener listener) {
        mLinkListener = listener;
    }

    public void setReplyContent(@NonNull List<ReplyEntity> data) {
        mData.clear();
        mData.addAll(data);
        createView();
        requestLayout();
    }


    private void createView() {
        removeAllViews();
        for (ReplyEntity entity : mData) {
            addView(createChild(entity));
        }
    }

    private RelativeLayout createChild(ReplyEntity entity) {
        RelativeLayout rl = new RelativeLayout(getContext());
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(llp);
        rl.addView(createContent(entity));
        rl.addView(createByReply(entity));
        rl.addView(createReply(entity));
        return rl;
    }

    private TextView createContent(ReplyEntity entity) {
        TextView tx = new TextView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tx.setLayoutParams(lp);
        tx.setGravity(Gravity.LEFT);
        tx.setTextColor(mTextColor);
        String str = entity.getReply() + "回复" + entity.getByReply();
        SpannableString sps = new SpannableString(str);
        sps.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.append(sps);
        String content = "：" + entity.getReplyContent() + "  " + entity.getStr();
        SpannableString other = new SpannableString(content);
        int index = content.indexOf(entity.getStr());
        other.setSpan(new ForegroundColorSpan(mOtherColor), index, index + entity.getStr().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.append(other);
        tx.setOnClickListener(new ContentClick(entity));
        return tx;
    }

    private TextView createReply(ReplyEntity entity) {
        TextView tx = new TextView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tx.setLayoutParams(lp);
        tx.setGravity(Gravity.LEFT);
        tx.setTextColor(mTextColor);
        String str = entity.getReply() + "回复";
        SpannableString sps = new SpannableString(str);
        createLinkText(sps, str, entity.getReply(), entity.getReplyId());
        tx.setHighlightColor(Color.TRANSPARENT);
        tx.setMovementMethod(LinkMovementMethod.getInstance());
        tx.setLinkTextColor(createColor());
        tx.append(sps);
        return tx;
    }

    private TextView createByReply(ReplyEntity entity) {
        TextView tx = new TextView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tx.setLayoutParams(lp);
        tx.setGravity(Gravity.LEFT);
        tx.setTextColor(mTextColor);
        String str = entity.getReply() + "回复";
        SpannableString sps = new SpannableString(str);
        sps.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.append(sps);
        String linkStr = entity.getByReply();
        SpannableString link = new SpannableString(linkStr);
        createLinkText(link, linkStr, entity.getByReply(), entity.getByReplyId());
        tx.setHighlightColor(Color.TRANSPARENT);
        tx.setMovementMethod(LinkMovementMethod.getInstance());
        tx.setLinkTextColor(createColor());
        tx.append(link);

        return tx;
    }

    private ColorStateList createColor() {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        states[0] = new int[]{android.R.attr.state_pressed};
        colors[0] = Color.parseColor("#A5A5A5");
        states[1] = new int[]{};
        colors[1] = Color.parseColor("#4E85DB");
        return new ColorStateList(states, colors);
    }

    private void createLinkText(SpannableString sps, String text, String clickStr, int id) {
        CustomClickSpan spClickByReply = new CustomClickSpan(id);
        int index = text.indexOf(clickStr);
        // 设置超链接
        sps.setSpan(spClickByReply, index, index + clickStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    class ContentClick implements View.OnClickListener {
        ReplyEntity entity;

        ContentClick(ReplyEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            if (mLinkListener != null) {
                mLinkListener.onReplyContentClick(v, entity);
            }
        }
    }

    class CustomClickSpan extends ClickableSpan {
        int id;

        CustomClickSpan(int id) {
            this.id = id;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (mLinkListener != null) {
                mLinkListener.onReplyClick(widget, id);
            }
        }
    }

    public static class ReplyEntity implements Parcelable {
        String reply; //回复人
        String byReply; //被回复人
        int replyId, byReplyId;
        String replyContent;  //回复内容
        String str = "";
        int id; //该条评论的id

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getByReply() {
            return byReply;
        }

        public void setByReply(String byReply) {
            this.byReply = byReply;
        }

        public int getReplyId() {
            return replyId;
        }

        public void setReplyId(int replyId) {
            this.replyId = replyId;
        }

        public int getByReplyId() {
            return byReplyId;
        }

        public void setByReplyId(int byReplyId) {
            this.byReplyId = byReplyId;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.reply);
            dest.writeString(this.byReply);
            dest.writeInt(this.replyId);
            dest.writeInt(this.byReplyId);
            dest.writeString(this.replyContent);
            dest.writeString(this.str);
            dest.writeInt(this.id);
        }

        public ReplyEntity() {
        }

        protected ReplyEntity(Parcel in) {
            this.reply = in.readString();
            this.byReply = in.readString();
            this.replyId = in.readInt();
            this.byReplyId = in.readInt();
            this.replyContent = in.readString();
            this.str = in.readString();
            this.id = in.readInt();
        }

        public static final Parcelable.Creator<ReplyEntity> CREATOR = new Parcelable.Creator<ReplyEntity>() {
            public ReplyEntity createFromParcel(Parcel source) {
                return new ReplyEntity(source);
            }

            public ReplyEntity[] newArray(int size) {
                return new ReplyEntity[size];
            }
        };
    }

}
