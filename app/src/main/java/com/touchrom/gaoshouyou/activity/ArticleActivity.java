package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.SharePreUtil;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.RichText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityArticleBinding;
import com.touchrom.gaoshouyou.dialog.ImgBrowseDialog;
import com.touchrom.gaoshouyou.dialog.ShareDialog;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.CollectModule;
import com.touchrom.gaoshouyou.module.NewsModule;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.popupwindow.FontSizePopupWindow;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2016/3/4.
 * 文章展示界面
 */
public class ArticleActivity extends BaseActivity<ActivityArticleBinding> implements FontSizePopupWindow.OnFontSizeChangeListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.detail)
    TextView mDetail;
    @InjectView(R.id.rich_text)
    RichText mRichText;
    @InjectView(R.id.collect)
    TextView mCollect;
    private int mArticleId;
    private ArticleEntity mEntity;
    private int mCollectId;
    private boolean mCollectState = false;
    private int mTypeId;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mArticleId = getIntent().getIntExtra("articleId", -1);
        mTypeId = getIntent().getIntExtra("typeId", -1);
        if (mArticleId == -1) {
            L.e(TAG, "请传入正确的文章Id");
            finish();
            return;
        }
        if (mTypeId == -1) {
            L.e(TAG, "请传入正确的文章typeId");
            finish();
            return;
        }
        showTempView(TempView.LOADING);
        getModule(NewsModule.class).getArticleContent(mTypeId, mArticleId);
        getModule(CollectModule.class).getCollectState(mTypeId, mArticleId);
    }

    private void setUpWidget(ArticleEntity entity) {
        mEntity = entity;
        mTitle.setText(entity.getTitle());
        mDetail.setText(entity.getTime() + " " + entity.getCompiler() + " " + entity.getNum() + "浏览 " + entity.getCommentNum() + "评论");
        mRichText.setOnImageClickListener(new RichText.OnImageClickListener() {

            @Override
            public void imageClicked(List<String> imageUrls, int position) {
                ImgBrowseDialog dialog = new ImgBrowseDialog(imageUrls, position);
                dialog.show(getSupportFragmentManager(), "imgDialog");
            }
        });
        mRichText.setOnURLClickListener(new RichText.OnURLClickListener() {
            @Override
            public boolean urlClicked(String url) {
                WebEntity web = new WebEntity();
                web.setContentUrl(url);
                TurnHelp.turnNormalWeb(ArticleActivity.this, web);
                return true;   //返回true表示事件已经使用
            }
        });

        mRichText.setImageFixListener(new RichText.ImageFixListener() {
            @Override
            public void onFix(RichText.ImageHolder holder) {

            }
        });
        mRichText.setRichText(entity.getContent());
        mToolbar.setTitle(entity.getArtType());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment:
                Intent comment = new Intent(this, CommentListActivity.class);
                comment.putExtra("appId", mArticleId);
                comment.putExtra("typeId", mEntity.getTypeId());
                startActivity(comment);
                break;
            case R.id.send_comment:
                Intent sendComment = new Intent(this, CommentActivity.class);
                sendComment.putExtra("appId", mArticleId);
                sendComment.putExtra("typeId", mEntity.getTypeId());
                startActivity(sendComment);
                break;
            case R.id.font_Setting:
                FontSizePopupWindow fontSetting = new FontSizePopupWindow(this);
                fontSetting.setOnFontSizeChangeListener(this);
                int y = (int) (AndroidUtils.getScreenParams(this)[1] - (getResources().getDimension(R.dimen.tab_bar_height) * 2));
                fontSetting.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, y);
                break;
            case R.id.share:
                ShareDialog dialog = new ShareDialog(SettingModule.SHARE_TYPE_ARTICLE, mEntity.getTypeId(), mArticleId);
                dialog.show(getSupportFragmentManager(), "文章分享");
                break;
            case R.id.collect:
                if (mCollectState) {
                    getModule(CollectModule.class).cancelCollect(mCollectId);
                } else {
                    getModule(CollectModule.class).addCollect(mTypeId, mArticleId);
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        SharePreUtil.putInt(Constance.APP.NAME, this, "fontSize", 14);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_ARTICLE_CONTENT) {
            setUpWidget((ArticleEntity) obj);
        } else if (result == ResultCode.SERVER.GET_COLLECT_STATE) {
            if (obj != null) {
                Bundle b = (Bundle) obj;
                mCollectId = b.getInt("collectId");
                mCollectState = b.getBoolean("state");
                mCollect.setText(mCollectState ? "取消收藏" : "收藏");
            }
        } else if (result == ResultCode.SERVER.ADD_COLLECT || result == ResultCode.SERVER.CANCEL_COLLECT) {
            getModule(CollectModule.class).getCollectState(mTypeId, mArticleId);
        }
    }

    @Override
    public void onChange(View view, float fontSize) {
        mRichText.setTextSize(fontSize);
    }
}
