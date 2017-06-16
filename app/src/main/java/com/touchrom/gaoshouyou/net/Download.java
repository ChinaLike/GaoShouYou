package com.touchrom.gaoshouyou.net;

import android.content.Context;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;


/**
 * Created by lk on 2015/11/12.
 * 文件下载
 */
public class Download {

    private Context mContext;

    public Download(Context context) {
        mContext = context;
    }

    private Download() {
    }

    /**
     * 下载图片
     *
     * @param imgUrl   下载链接
     * @param savePath 保存路径
     */
    public void downloadImg(String imgUrl, String savePath, DownloadListener listener) {
        DownLoadUtil util = new DownLoadUtil();
        util.download(mContext, imgUrl, savePath, listener);
    }

}
