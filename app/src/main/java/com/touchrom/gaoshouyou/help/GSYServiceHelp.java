package com.touchrom.gaoshouyou.help;

import android.content.Context;
import android.content.Intent;

import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.entity.GSYSEntity;
import com.touchrom.gaoshouyou.service.GaoShouYouService;

/**
 * Created by lk on 2016/1/14.
 * 高手游全局服务帮助类
 */
public class GSYServiceHelp {
    public static final String TAG = "GSYServiceHelp";
    private Context mContext;

    private GSYServiceHelp() {

    }

    public static GSYServiceHelp newInstance() {
        return new GSYServiceHelp();
    }

    /**
     * @param context
     * @param entity  命令实体
     */
    public void run(Context context, final GSYSEntity entity) {
        mContext = context;
        Intent intent = new Intent(context, GaoShouYouService.class);
        intent.putExtra(Constance.SERVICE.DOWNLOAD_CMD, entity.getAction());
        intent.putExtra(Constance.SERVICE.GSY_SERVICE_ENTITY, entity);
        context.startService(intent);
    }
}
