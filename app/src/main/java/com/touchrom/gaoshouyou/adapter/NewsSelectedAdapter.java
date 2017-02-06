package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.base.adapter.AbsRVAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.lyy.ui.widget.AutoScrollViewPager;
import com.lyy.ui.widget.CircleIndicator;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.entity.ArticleEntity;
import com.touchrom.gaoshouyou.entity.BannerEntity;
import com.touchrom.gaoshouyou.entity.NewsEntity;
import com.touchrom.gaoshouyou.fragment.BannerFragment;
import com.touchrom.gaoshouyou.help.ImgHelp;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/3/1.
 * 资讯精选Adapter
 */
public class NewsSelectedAdapter extends AbsRVAdapter<NewsEntity, NewsSelectedAdapter.MyHolder> {
    FragmentManager mFm;

    public NewsSelectedAdapter(Context context, List<NewsEntity> data, FragmentManager fm) {
        super(context, data);
        mFm = fm;
    }

    public NewsSelectedAdapter(Context context, List<NewsEntity> data) {
        super(context, data);
    }


    @Override
    protected MyHolder getViewHolder(View convertView, int viewType) {
        switch (viewType) {
            case NewsEntity.ITEM_BANNER:
                return new BannerHolder(convertView);
            case NewsEntity.ITEM_ARTICLE:
                return new ArticleHolder(convertView);
            case NewsEntity.ITEM_REVIEW_ARTICLE:
                return new ReviewsHolder(convertView);
        }
        return new MyHolder(convertView);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int setLayoutId(int type) {
        int layout = R.layout.item_simple_temp;
        switch (type) {
            case NewsEntity.ITEM_BANNER:
                layout = R.layout.item_news_banner;
                break;
            case NewsEntity.ITEM_ARTICLE:
                layout = R.layout.item_news_article;
                break;
            case NewsEntity.ITEM_REVIEW_ARTICLE:
                layout = R.layout.item_news_reviews;
                break;

        }
        return layout;
    }

    @Override
    protected void bindData(MyHolder holder, int position, NewsEntity item) {
        int type = getItemViewType(position);
        if (type == NewsEntity.ITEM_BANNER) {
            BannerHolder vpHolder = (BannerHolder) holder;
            handleBannerItem(vpHolder, position, item);
        } else if (type == NewsEntity.ITEM_ARTICLE) {
            handleArticleItem((ArticleHolder) holder, position, item);
        } else if (type == NewsEntity.ITEM_REVIEW_ARTICLE) {
            handleReviewItem((ReviewsHolder) holder, position, item);
        }
        if (holder.divider != null) {
            holder.divider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }
    }

    private void handleReviewItem(ReviewsHolder holder, int position, NewsEntity entity) {
        NewsEntity.ReviewArticleEntity reEntity = entity.getReviewArticle();
        ImgHelp.setImg(getContext(), reEntity.getImgUrl(), R.mipmap.default_banner, holder.img);
        holder.title.setText(reEntity.getTitle());
        holder.time.setText(reEntity.getTime() + "发布");
        holder.num.setText(reEntity.getNum() + "浏览");
        holder.muText.setText("画面音质：" + reEntity.getMuNum());
        holder.ieText.setText("交互体验：" + reEntity.getIeNum());
        holder.playText.setText("玩法创意：" + reEntity.getPlayNum());
        holder.complexNum.setText(reEntity.getComplexNum());
    }

    private void handleArticleItem(ArticleHolder holder, int position, NewsEntity entity) {
        ArticleEntity article = entity.getArticle();
        Glide.with(mContext).load(article.getImgUrl()).into(holder.img);
        holder.title.setText(article.getTitle());
        holder.time.setText(article.getTime() + "发布");
        holder.num.setText(article.getNum() + "浏览");
    }

    private void handleBannerItem(final BannerHolder holder, int position, NewsEntity entity) {
        SimpleViewPagerAdapter spa = (SimpleViewPagerAdapter) holder.vp.getTag(holder.vp.getId());
        if (spa == null) {
            spa = new SimpleViewPagerAdapter(mFm);
            holder.vp.setTag(holder.vp.getId(), spa);
            int i = 0;
            final List<BannerEntity> bannerEntities = entity.getBanners();
            for (BannerEntity bannerEntity : bannerEntities) {
                BannerFragment fragment = BannerFragment.newInstance(bannerEntity);
                fragment.setCanClick(true);
                spa.addFrag(fragment, "banner" + i);
                i++;
            }
            holder.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    holder.title.setText(bannerEntities.get(position).getTitle());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            holder.vp.setAdapter(spa);
            holder.indicator.setViewPager(holder.vp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    class MyHolder extends AbsRVHolder {
        View divider;

        public MyHolder(View view) {
            super(view);
            divider = view.findViewById(R.id.gray_divider);
        }
    }

    class ReviewsHolder extends MyHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.mu_text)
        TextView muText;
        @InjectView(R.id.ie_text)
        TextView ieText;
        @InjectView(R.id.play_text)
        TextView playText;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.complex_num)
        TextView complexNum;

        public ReviewsHolder(View view) {
            super(view);
        }
    }

    class BannerHolder extends MyHolder {
        @InjectView(R.id.vp)
        AutoScrollViewPager vp;
        @InjectView(R.id.indicator)
        CircleIndicator indicator;
        @InjectView(R.id.title)
        TextView title;

        public BannerHolder(View view) {
            super(view);
        }
    }

    class ArticleHolder extends MyHolder {
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.num)
        TextView num;

        public ArticleHolder(View view) {
            super(view);
        }
    }
}
