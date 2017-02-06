package com.touchrom.gaoshouyou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.SharePreUtil;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.NoScrollListView;
import com.lyy.ui.widget.RichText;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.adapter.game_info_adapter.GameInfoAdapter;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.ActivityTopicDetailBinding;
import com.touchrom.gaoshouyou.dialog.ImgBrowseDialog;
import com.touchrom.gaoshouyou.dialog.ShareDialog;
import com.touchrom.gaoshouyou.entity.GameInfoEntity;
import com.touchrom.gaoshouyou.entity.TopicDetailEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.NewsModule;
import com.touchrom.gaoshouyou.module.SettingModule;
import com.touchrom.gaoshouyou.popupwindow.FontSizePopupWindow;
import com.touchrom.gaoshouyou.widget.TempView;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/4.
 * 文章展示界面
 */
public class TopicDetailActivity extends BaseActivity<ActivityTopicDetailBinding> implements FontSizePopupWindow.OnFontSizeChangeListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.detail)
    TextView mDetail;
    @InjectView(R.id.rich_text)
    RichText mRichText;
    @InjectView(R.id.bar_layout)
    RelativeLayout mBarLayout;
    @InjectView(R.id.bar_list)
    NoScrollListView mArtList;
    @InjectView(R.id.game_list)
    NoScrollListView mGameList;
    @InjectView(R.id.scroll_view)
    ScrollView mSView;
    private int mTopicId;
    private GameInfoAdapter mGameAdapter;
    private SimpleAdapter<TopicDetailEntity.BarEntity> mArtAdapter;
    private TopicDetailEntity mEntity;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTopicId = getIntent().getIntExtra("topicId", -1);
        if (mTopicId == -1) {
            L.e(TAG, "请传入正确的专题Id");
            finish();
            return;
        }
        showTempView(TempView.LOADING);
        getModule(NewsModule.class).getTopicDetailContent(mTopicId);
    }

    private void setUpWidget(TopicDetailEntity entity) {
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
                TurnHelp.turnNormalWeb(TopicDetailActivity.this, web);
                return true;   //返回true表示事件已经使用
            }
        });

        mRichText.setImageFixListener(new RichText.ImageFixListener() {
            @Override
            public void onFix(RichText.ImageHolder holder) {

            }
        });
        mRichText.setRichText(entity.getDescription());
        mToolbar.setTitle(entity.getArtType());
        if (mEntity.getBars() == null || mEntity.getBars().size() == 0) {
            mBarLayout.setVisibility(View.GONE);
        } else {
            mBarLayout.setVisibility(View.VISIBLE);
            setUpArtBarList(mEntity.getBars());
        }
        setUpGameList(mEntity.getGames());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSView.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 100);
    }

    private void setUpGameList(final List<GameInfoEntity> list) {
        mGameAdapter = new GameInfoAdapter(this, list);
        mGameList.setAdapter(mGameAdapter);
        mGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurnHelp.turn(TopicDetailActivity.this, list.get(position));
            }
        });
    }

    private void setUpArtBarList(final List<TopicDetailEntity.BarEntity> list) {
        mArtAdapter = new SimpleAdapter<TopicDetailEntity.BarEntity>(this, list, R.layout.item_topic_detail_art) {
            @Override
            public void convert(SimpleViewHolder helper, TopicDetailEntity.BarEntity item) {
                helper.getView(R.id.root).setBackgroundColor(helper.getPosition() % 2 == 0 ? Color.WHITE : Color.parseColor("#F6F6F8"));
                helper.setText(R.id.text, item.getArtTitle());
            }
        };
        mArtList.setAdapter(mArtAdapter);
        mArtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicDetailEntity.BarEntity entity = list.get(position);
                TurnHelp.turnArticle(TopicDetailActivity.this, entity.getTypeId(), entity.getArtId());
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment:
                Intent comment = new Intent(this, CommentListActivity.class);
                comment.putExtra("appId", mTopicId);
                comment.putExtra("typeId", mEntity.getTypeId());
                startActivity(comment);
                break;
            case R.id.send_comment:
                Intent sendComment = new Intent(this, CommentActivity.class);
                sendComment.putExtra("appId", mTopicId);
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
                ShareDialog dialog = new ShareDialog(SettingModule.SHARE_TYPE_ARTICLE, mEntity.getTypeId(), mTopicId);
                dialog.show(getSupportFragmentManager(), "文章分享");
                break;
            case R.id.more:
                Intent aIntent = new Intent(this, TopicAllArtActivity.class);
                aIntent.putExtra("artId", mEntity.getArticleId());
                startActivity(aIntent);
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
        if (result == ResultCode.SERVER.GET_TOPIC_DETAIL_CONTENT) {
            setUpWidget((TopicDetailEntity) obj);
        }
    }

    @Override
    public void onChange(View view, float fontSize) {
        mRichText.setTextSize(fontSize);
    }
}
