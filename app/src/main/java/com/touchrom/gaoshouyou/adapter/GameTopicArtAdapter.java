package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsOrdinaryViewHolder;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/7.
 * 专题所有文章适配器
 */
public class GameTopicArtAdapter extends AbsOrdinaryAdapter<NewsEntity, GameTopicArtAdapter.TopicArtHolder> {

    public GameTopicArtAdapter(Context context, List<NewsEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_news_article;
    }

    @Override
    public void bindData(int position, TopicArtHolder holder, NewsEntity entity) {
        ArticleEntity article = entity.getArticle();
        Glide.with(getContext()).load(article.getImgUrl()).into(holder.img);
        holder.title.setText(article.getTitle());
        holder.time.setText(article.getTime() + "发布");
        holder.num.setText(article.getNum() + "浏览");
        holder.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public TopicArtHolder getViewHolder(View convertView) {
        return new TopicArtHolder(convertView);
    }

    class TopicArtHolder extends AbsOrdinaryViewHolder {
        @InjectView(R.id.gray_divider)
        View divider;
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.num)
        TextView num;

        public TopicArtHolder(View view) {
            super(view);
        }
    }
}
