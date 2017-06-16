package com.touchrom.gaoshouyou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchrom.gaoshouyou.base.adapter.AbsRVAdapter;
import com.touchrom.gaoshouyou.base.adapter.AbsRVHolder;
import com.lyy.ui.widget.HorizontalProgressBarWithNumber;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.entity.SettingEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.ApkUpdateInfoEntity;
import com.touchrom.gaoshouyou.entity.sql.DownloadEntity;
import com.touchrom.gaoshouyou.fragment.AMApkFragment;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.ImgHelp;
import com.touchrom.gaoshouyou.inf.OnAMAdapterListener;
import com.touchrom.gaoshouyou.service.DownloadService;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lk on 2015/12/22.
 * 应用管理的可更新，已安装，安装包的Adapter
 */
public class AppManagerAdapter extends AbsRVAdapter<ApkInfoEntity, AppManagerAdapter.AppManagerHolder> {
    private int mType;
    private SparseBooleanArray mCheck = new SparseBooleanArray();
    private OnAMAdapterListener mListener;
    private Set<ApkInfoEntity> mCheckData = new HashSet<>();
    private SettingEntity mSettingEntity;
    /*
     *下载相关的参数
     */
    private Map<String, Long> mProgress = new HashMap<>();
    private Map<String, SpeedParams> mSpeedP = new HashMap<>();
    private Drawable mOrangeDrawable, mGreenDrawable, mYellowDrawable, mGreyDrawable;
    /**
     * 保存当前下载的Item
     */
    private SparseIntArray mDownloadingItem = new SparseIntArray(2);

    /**
     * @param context
     * @param data
     * @param type    {@link AMApkFragment#ALL_APK}
     *                {@link AMApkFragment#INSTALLED_APK}
     *                {@link AMApkFragment#UPDATE_APK}
     */
    public AppManagerAdapter(Context context, List<ApkInfoEntity> data, int type) {
        super(context, data);
        mType = type;
        for (int i = 0, count = data.size(); i < count; i++) {
            mCheck.append(i, false);
        }
        mSettingEntity = AppManager.getInstance().getSetting();
        mOrangeDrawable = getContext().getResources().getDrawable(R.drawable.shap_orange);
        mGreenDrawable = getContext().getResources().getDrawable(R.drawable.shap_blue);
        mYellowDrawable = getContext().getResources().getDrawable(R.drawable.shap_yellow);
        mGreyDrawable = getContext().getResources().getDrawable(R.drawable.shap_grey);
    }


    /**
     * fragment setUp Adapter数据前需要执行这方法
     */
    public void pre() {
        if (mType == AMApkFragment.DOWNLOAD_APK) {
            for (ApkInfoEntity entity : mData) {
                DownloadEntity downloadEntity = entity.getDownloadEntity();
                long p = downloadEntity.getCurrentProgress();
                SpeedParams params = new SpeedParams();
                params.lastP = p;
                params.lastTime = System.currentTimeMillis();
                params.speed = 0;
                mSpeedP.put(downloadEntity.getDownloadUrl(), params);
                if (p != 0) {
                    mProgress.put(downloadEntity.getDownloadUrl(), p);
                }
            }
        }
        mSettingEntity = AppManager.getInstance().getSetting();
        setDownloadingItem();
    }

    /**
     * 将下载实体存储到数据库
     *
     * @param downloadEntity
     */
    private void saveDownloadEntity2Db(DownloadEntity downloadEntity) {
        List<DownloadEntity> list = DataSupport.where("downloadUrl=?", downloadEntity.getDownloadUrl()).find(DownloadEntity.class);
        if (list == null || list.size() == 0) {
            downloadEntity.save();
        } else {
            downloadEntity.updateAll("downloadUrl=?", downloadEntity.getDownloadUrl());
        }
    }

    @Override
    protected AppManagerHolder getViewHolder(View convertView, int viewType) {
        return new AppManagerHolder(convertView);
    }

    @Override
    protected int setLayoutId(int type) {
        return mType == AMApkFragment.DOWNLOAD_APK ? R.layout.item_am_update_app : R.layout.item_am_normal_apk;
    }

    public ApkInfoEntity getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    /**
     * 添加一组数据
     */
    public void addItem(List<ApkInfoEntity> entities) {
        if (mType == AMApkFragment.DOWNLOAD_APK) {
            for (ApkInfoEntity entity : entities) {
                int j = 0;
                for (int i = 0, count = mData.size(); i < count; i++) {
                    if (!entity.getDownloadEntity().getDownloadUrl().equals(mData.get(i).getDownloadEntity().getDownloadUrl())) {
                        j++;
                    } else {
                        break;
                    }
                }
                if (j == mData.size()) {
                    mData.add(entity);
                }
            }
        } else {
            mData.addAll(entities);
        }
        clearCheckState();
        if (mListener != null) {
            mListener.onItemNumChange(mType, mData.size());
        }
    }

    /**
     * 添加数据
     */
    public void addItem(ApkInfoEntity entity) {
        if (mType == AMApkFragment.DOWNLOAD_APK) {
            for (int i = 0, count = mData.size(); i < count; i++) {
                if (entity.getDownloadEntity().getDownloadUrl().equals(mData.get(i).getDownloadEntity().getDownloadUrl())) {
                    return;
                }
            }
        }
        mData.add(entity);
        clearCheckState();
        if (mListener != null) {
            mListener.onItemNumChange(mType, mData.size());
        }
    }

    @Override
    public void bindData(AppManagerHolder holder, int position, ApkInfoEntity item) {
        //button监听
        ButtonClickListener btListener = (ButtonClickListener) holder.bt.getTag(holder.bt.getId());
        if (btListener == null) {
            btListener = new ButtonClickListener();
        }
        //checkBox监听
        CheckBoxCheckListener cbListener = (CheckBoxCheckListener) holder.checkBox.getTag(holder.checkBox.getId());
        if (cbListener == null) {
            cbListener = new CheckBoxCheckListener();
        }
        cbListener.setCheckData(position, item);
        holder.checkBox.setOnCheckedChangeListener(cbListener);
        holder.checkBox.setChecked(mCheck.get(position));
        holder.checkBox.setTag(holder.checkBox.getId(), cbListener);

        if (mType == AMApkFragment.INSTALLED_APK || mType == AMApkFragment.ALL_APK) {  //可更新、已安装、安装包
            handleNormalHolder(holder, item);
        } else if (mType == AMApkFragment.UPDATE_APK) {
            handleUpdateHolder(holder, item.getUpdateInfoEntity());
        } else {    //下载的
            handleDownloadHolder(holder, item.getDownloadEntity());
        }

        btListener.setCheckData(position, item);
        holder.bt.setOnClickListener(btListener);
        holder.bt.setTag(holder.bt.getId(), btListener);
    }

    /**
     * 操作更新的holder
     */
    private void handleUpdateHolder(AppManagerHolder holder, ApkUpdateInfoEntity entity) {
        holder.name.setText(entity.getAppName());
        String str = entity.getOldVersionName() + " > " + entity.getNewVersionName();
        holder.info.setText(StringUtil.highLightStr(str, entity.getNewVersionName(), Color.parseColor("#0cc6c6")));
        holder.installState.setText(entity.getSize());
        if (mSettingEntity.isShowImg()) {
            ImgHelp.setImg(getContext(), entity.getImgUrl(), holder.icon);
        }
        int btBg = R.drawable.selector_apk_orange_bt;
        String btText = "更新";
        holder.bt.setEnabled(true);
        int state = entity.getUpdateState();
        if (state == ApkUpdateInfoEntity.STATE_UPDATING) {
            btBg = R.drawable.selector_apk_un_grey_bt;
            btText = "更新中";
            holder.bt.setEnabled(false);
        } else if (state == ApkUpdateInfoEntity.STATE_UPDATE_COMPLETE) {
            btBg = R.drawable.selector_apk_yellow_bt;
            btText = "安装";
        }
        holder.bt.setText(btText);
        holder.bt.setBackgroundDrawable(getContext().getResources().getDrawable(btBg));
    }

    /**
     * 操作安装和已安装的holder
     */
    private void handleNormalHolder(AppManagerHolder holder, ApkInfoEntity item) {
        holder.name.setText(item.getApkName());
        Drawable icon = null;
        CharSequence info = "";
        String state = "";
        Drawable btBg = null;
        String btText = "";
        if (mType == AMApkFragment.ALL_APK) {
            info = item.getApkVersion() + "  " + item.getApkSize();
            state = item.isApkInstallState() ? "已安装" : "未安装";
            btBg = getContext().getResources().getDrawable(R.drawable.selector_apk_yellow_bt);
            btText = "安装";
            icon = FileUtil.getApkIcon(getContext(), item.getApkPath());
        } else if (mType == AMApkFragment.INSTALLED_APK) {
            info = item.getApkVersion();
            state = item.getApkSize();
            btBg = getContext().getResources().getDrawable(R.drawable.selector_apk_un_grey_bt);
            btText = "卸载";
            icon = item.getPackageInfo().applicationInfo.loadIcon(getContext().getPackageManager());
        }
        if (icon != null) {
            holder.icon.setImageDrawable(icon);
        }
        holder.info.setText(info);
        holder.installState.setText(state);
        if (item.isApkInstallState() && mType == AMApkFragment.ALL_APK) {
            holder.bt.setVisibility(View.GONE);
        } else {
            holder.bt.setVisibility(View.VISIBLE);
            holder.bt.setText(btText);
            if (btBg != null) {
                holder.bt.setBackgroundDrawable(btBg);
            }
        }
    }

    /**
     * 操作下载的holder
     *
     * @param holder
     * @param entity
     */
    private void handleDownloadHolder(AppManagerHolder holder, DownloadEntity entity) {
        holder.name.setText(entity.getName());
        holder.downloadSize.setText(entity.getSize());
        if (mSettingEntity.isShowImg()) {
            Glide.with(getContext()).load(entity.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.default_icon)
                    .fitCenter()
                    .into(holder.icon);
        }

        switch (entity.getState()) {
            case DownloadEntity.STATE_COMPLETE: //处理完成的状态
                if (entity.isInstalled()) {
                    holder.bt.setBackgroundDrawable(mGreenDrawable);
                    holder.downloadState.setText("已安装");
                    holder.bt.setText("打开");
                } else if (entity.isDownloadComplete()) {
                    holder.bt.setBackgroundDrawable(mYellowDrawable);
                    holder.downloadState.setText("下载完成");
                    holder.bt.setText("安装");
                }
                break;
            case DownloadEntity.STATE_FAIL:
            case DownloadEntity.STATE_UN_COMPLETE:
            case DownloadEntity.STATE_WAIT:
                holder.bt.setBackgroundDrawable(mOrangeDrawable);
                holder.downloadState.setText("等待");
                holder.bt.setText("开始");
                break;
            case DownloadEntity.STATE_DOWNLOAD_ING:
                holder.bt.setBackgroundDrawable(mGreyDrawable);
                holder.downloadState.setText("下载中");
                holder.bt.setText("暂停");
                break;
            case DownloadEntity.STATE_STOP:
                holder.bt.setBackgroundDrawable(mOrangeDrawable);
                holder.downloadState.setText("暂停");
                holder.bt.setText("继续");
                break;
        }

        if (entity.getState() != DownloadEntity.STATE_COMPLETE) {
            holder.progress.setVisibility(View.VISIBLE);
            holder.completeTime.setVisibility(View.GONE);
            Long p = mProgress.get(entity.getDownloadUrl());
            if (p != null) {
                holder.progress.setProgress((int) (p * 100 / entity.getMaxLen()));
                updateSpeed(holder, entity);
            } else {
                holder.progress.setProgress(0);
            }
        } else {
            holder.progress.setVisibility(View.INVISIBLE);
            holder.completeTime.setVisibility(View.VISIBLE);
            holder.speed.setText("");
            holder.completeTime.setText(entity.getCompleteTime());
        }
    }

    private void updateSpeed(AppManagerHolder holder, DownloadEntity entity) {
        Long p = mProgress.get(entity.getDownloadUrl());
        SpeedParams params = mSpeedP.get(entity.getDownloadUrl());
        if (params != null) {
            if (System.currentTimeMillis() - params.lastTime > 1000 && entity.getState() == DownloadEntity.STATE_DOWNLOAD_ING) {
                params.lastTime = System.currentTimeMillis();
                long speed = p - params.lastP;
                if (speed == 0) {
                    return;
                }
                params.lastP = p;
                params.speed = speed;
            }
            holder.speed.setText(entity.getState() == DownloadEntity.STATE_DOWNLOAD_ING ?
                    FileUtil.formatFileSize(params.speed <= 0 ? 0 : params.speed) + "/s" : "");
        }
    }


    /**
     * 设置进度条数据
     *
     * @param key      下载链接
     * @param progress 下载进度
     */
    public void setProgress(String key, long progress) {
        mProgress.put(key, progress);

    }

    /**
     * 更新进度条
     */
    public void updateProgress(int start, int end) {
        for (int i = 0; i < 2; i++) {
            int p = mDownloadingItem.get(i, -1);
            if (p == -1 || p < start || p > end) {
                continue;
            }
            notifyItemChanged(mDownloadingItem.get(i));
        }
    }


    /**
     * 设置当前正在下载的Item
     */
    private void setDownloadingItem() {
        if (mType != AMApkFragment.DOWNLOAD_APK) {
            return;
        }
        int j = 0;
        for (int i = 0, count = mData.size(); i < count; i++) {
            if (mData.get(i).getDownloadEntity().getState() == DownloadEntity.STATE_DOWNLOAD_ING) {
                mDownloadingItem.put(j, i);
                j++;
            }
        }
    }

    /**
     * 更新下载状态
     */
    public void updateDownloadState(DownloadEntity entity) {
        int l = indexOfDownloadEntity(entity);
        if (l != -1) {
            mData.get(l).setDownloadEntity(entity);
        }
        notifyDataSetChanged();
        setDownloadingItem();
    }

    /**
     * 列表里面下载实体所在的位置
     *
     * @param entity
     * @return
     */
    private int indexOfDownloadEntity(DownloadEntity entity) {
        for (int i = 0, count = mData.size(); i < count; i++) {
            if (mData.get(i).getDownloadEntity().getDownloadUrl().equals(entity.getDownloadUrl())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 清空选中状态
     */
    public void clearCheckState() {
        mCheckData.clear();
        mCheck.clear();
        for (int i = 0, count = mData.size(); i < count; i++) {
            mCheck.put(i, false);
        }
        notifyDataSetChanged();
        setDownloadingItem();
    }

    /**
     * 忽略指定任务
     *
     * @param key 下载连接
     */
    public void ignoreTask(String key) {
        List<DownloadEntity> list = DataSupport.where("downloadUrl=?", key).find(DownloadEntity.class);
        if (list != null && list.size() > 0) {
            ApkInfoEntity back = null;
            for (ApkInfoEntity entity : mData) {
                if (entity.getDownloadEntity().getDownloadUrl().equals(key)) {
                    back = entity;
                    break;
                }
            }
            removeTask(list.get(0));
            if (back != null) {
                mData.remove(back);
            }
            clearCheckState();
            if (mListener != null) {
                mListener.onItemNumChange(mType, mData.size());
            }
        }
    }

    /**
     * 删除指定的item
     */
    public void removeItem(ApkInfoEntity entity) {
        if (mType == AMApkFragment.DOWNLOAD_APK) {
            DownloadEntity dEntity = entity.getDownloadEntity();
            removeTask(dEntity);
        } else if (mType == AMApkFragment.UPDATE_APK) {
            ApkUpdateInfoEntity updateEntity = entity.getUpdateInfoEntity();
            List<ApkUpdateInfoEntity> list = DataSupport.where("downloadUrl=?", updateEntity.getDownloadUrl()).find(ApkUpdateInfoEntity.class);
            if (list.size() > 0) {
                ApkUpdateInfoEntity entity1 = list.get(0);
                entity1.setUpdateState(ApkUpdateInfoEntity.STATE_UPDATE_IGNORE);
                entity1.updateAll("downloadUrl=?", updateEntity.getDownloadUrl());
            }
        } else if (mType == AMApkFragment.ALL_APK) {
            ApkInfoEntity.deleteAll(ApkInfoEntity.class, "apkPackage=?", entity.getApkPackage());
        }
        mData.remove(entity);
        clearCheckState();
        if (mListener != null) {
            mListener.onItemNumChange(mType, mData.size());
        }
    }

    /**
     * 删除选中的item
     */
    public void removeAllCheckedItem() {
        if (mType == AMApkFragment.DOWNLOAD_APK) {
            for (ApkInfoEntity entity : mCheckData) {
                DownloadEntity dEntity = entity.getDownloadEntity();
                removeTask(dEntity);
            }
        }
        for (ApkInfoEntity entity : mCheckData) {
            mData.remove(entity);
        }
        clearCheckState();
        if (mListener != null) {
            mListener.onItemNumChange(mType, mData.size());
        }
    }

    /**
     * 设置选择监听
     *
     * @param listener
     */
    public void setOnAMAdapterListener(OnAMAdapterListener listener) {
        mListener = listener;
    }

    /**
     * 按钮监听
     */
    class ButtonClickListener implements View.OnClickListener {
        int position;
        ApkInfoEntity apkInfoEntity;

        public void setCheckData(int position, ApkInfoEntity entity) {
            this.position = position;
            apkInfoEntity = entity;
        }

        @Override
        public void onClick(View v) {
            clearCheckState();
            if (mType == AMApkFragment.ALL_APK) {
                if (mListener != null) {
                    mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_INSTALL, apkInfoEntity);
                }
            } else if (mType == AMApkFragment.INSTALLED_APK) {
                if (mListener != null) {
                    mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_UN_INSTALL, apkInfoEntity);
                }
            } else if (mType == AMApkFragment.DOWNLOAD_APK) {
                DownloadEntity dEntity = apkInfoEntity.getDownloadEntity();
                int state = dEntity.getState();
                if (state == DownloadEntity.STATE_COMPLETE) {
                    if (mListener != null) {
                        if (dEntity.isInstalled()) {
                            mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_OPEN, apkInfoEntity);
                        } else if (dEntity.isDownloadComplete()) {
                            mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_INSTALL, apkInfoEntity);
                        }
                    }
                } else {
                    startService(dEntity);
                }
            } else if (mType == AMApkFragment.UPDATE_APK) {
                if (mListener != null) {
                    int state = apkInfoEntity.getUpdateInfoEntity().getUpdateState();
                    if (state == ApkUpdateInfoEntity.STATE_UN_UPDATE) {
                        mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_UPDATE, apkInfoEntity);
                    } else if (state == ApkUpdateInfoEntity.STATE_UPDATE_COMPLETE) {
                        mListener.onButtonHandle(mType, OnAMAdapterListener.HANDLER_INSTALL, apkInfoEntity);
                    }
                }
            }
        }
    }

    /**
     * 启动服务
     */
    private void startService(final DownloadEntity entity) {
        switch (entity.getState()) {
            case DownloadEntity.STATE_DOWNLOAD_ING:
                DownloadHelp.newInstance().downloadCmd(getContext(), entity, DownloadService.ACTION_STOP);
                break;
            case DownloadEntity.STATE_FAIL:
            case DownloadEntity.STATE_STOP:
            case DownloadEntity.STATE_WAIT:
            case DownloadEntity.STATE_UN_COMPLETE:
                DownloadHelp.newInstance().downloadCmd(getContext(), entity, DownloadService.ACTION_START);
                break;
        }

    }

    /**
     * 删除下载任务
     */
    private void removeTask(DownloadEntity entity) {
        if ((entity.isInstalled() || entity.isDownloadComplete())) {
            DataSupport.deleteAll(DownloadEntity.class, "downloadUrl=?", entity.getDownloadUrl());
        } else {
            if (AndroidUtils.isServiceRun(getContext(), DownloadService.class)) {
                DownloadHelp.newInstance().downloadCmd(getContext(), entity, DownloadService.ACTION_REMOVE_TASK);
            } else {
                DataSupport.deleteAll(DownloadEntity.class, "downloadUrl=?", entity.getDownloadUrl());
                File file = new File(mSettingEntity.getDownloadLocation() + entity.getName() + ".apk");
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * checkBox监听
     */
    class CheckBoxCheckListener implements CompoundButton.OnCheckedChangeListener {

        int position;
        ApkInfoEntity apkInfoEntity;

        public void setCheckData(int position, ApkInfoEntity entity) {
            this.position = position;
            apkInfoEntity = entity;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mCheck.put(position, isChecked);
            if (isChecked) {
                mCheckData.add(apkInfoEntity);
            } else {
                mCheckData.remove(apkInfoEntity);
            }
            if (mListener != null) {
                mListener.onCheck(mType, mCheckData.size(), mCheckData);
            }
        }
    }

    private class SpeedParams {
        long lastP;
        long lastTime;
        long speed;
    }

    public class AppManagerHolder extends AbsRVHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView name;
        TextView info;
        TextView installState;
        Button bt;
        TextView downloadState;
        TextView downloadSize;
        HorizontalProgressBarWithNumber progress;
        TextView completeTime;
        TextView speed;

        public AppManagerHolder(View itemView) {
            super(itemView);
            speed = (TextView) itemView.findViewById(R.id.download_speed);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            info = (TextView) itemView.findViewById(R.id.info);
            installState = (TextView) itemView.findViewById(R.id.install_state);
            bt = (Button) itemView.findViewById(R.id.bt);
            downloadState = (TextView) itemView.findViewById(R.id.download_state);
            downloadSize = (TextView) itemView.findViewById(R.id.download_size);
            progress = (HorizontalProgressBarWithNumber) itemView.findViewById(R.id.progress);
            completeTime = (TextView) itemView.findViewById(R.id.complete_time);
        }
    }
}
