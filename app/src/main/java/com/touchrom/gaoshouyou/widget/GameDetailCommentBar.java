package com.touchrom.gaoshouyou.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.http.HttpUtil;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.CommentActivity;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lyy on 2016/3/14.
 * 游戏详情评论bar
 */
public class GameDetailCommentBar extends RelativeLayout {
    TextView mNumText;
    int mGameId;

    public GameDetailCommentBar(Context context, int gameId) {
        super(context, null);
        mGameId = gameId;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_game_detail_comment_reply, this, true);
        mNumText = (TextView) findViewById(R.id.num_text);
        View et = findViewById(R.id.temp);
        et.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof BaseActivity) {
                    BaseActivity activity = (BaseActivity) getContext();
                    Intent intent = new Intent(activity, CommentActivity.class);
                    intent.putExtra("appId", mGameId);
                    intent.putExtra("typeId", 0);
                    activity.startActivityForResult(intent, ResultCode.ACTIVITY.GD_COMMENT);
                }
            }
        });
        notifyData();
    }

    public void notifyData() {
        final ServiceUtil util = ServiceUtil.getInstance(getContext());
        Map<String, String> params = new HashMap<>();
        params.put("appId", mGameId + "");
        params.put("typeId", 0 + "");
        util.getCommentTotal(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (util.isRequestSuccess(obj)) {
                        JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        int num = content.getInt("total");
                        Observable.just(num).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                setCommentNum(integer);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCommentNum(int num) {
        mNumText.setText("已有" + num + "条评论");
    }
}
