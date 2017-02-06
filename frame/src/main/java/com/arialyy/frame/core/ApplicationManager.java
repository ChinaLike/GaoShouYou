package com.arialyy.frame.core;

import android.app.ActivityManager;
import android.content.Context;

import com.arialyy.frame.util.show.FL;

import java.util.Stack;

/**
 * Created by lyy on 2015/11/4.
 * APP生命周期管理类管理
 */
public class ApplicationManager {
    private static final String TAG = "ApplicationManager";
    private static final Object LOCK = new Object();
    private volatile static ApplicationManager mManager = null;
    private Context mContext;
    private Stack<AbsActivity> mActivityStack = new Stack<>();

    private ApplicationManager() {

    }

    private ApplicationManager(Context context) {
        mContext = context;
    }

    protected static ApplicationManager getInstance(Context context) {
        if (mManager == null) {
            synchronized (LOCK) {
                if (mManager == null) {
                    mManager = new ApplicationManager(context);
                }
            }
        }
        return mManager;
    }

    public Stack<AbsActivity> getActivityStack() {
        return mActivityStack;
    }

    /**
     * 堆栈大小
     */
    public int getActivitySize() {
        return mActivityStack.size();
    }

    /**
     * 获取指定的Activity
     */
    public AbsActivity getActivity(int location) {
        return mActivityStack.get(location);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AbsActivity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public AbsActivity getCurrentActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(mActivityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AbsActivity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(AbsActivity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (AbsActivity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (mActivityStack.get(i) != null && mActivityStack.size() > 0) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
        } catch (Exception e) {
            FL.e(TAG, FL.getPrintException(e));
        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground) {
                System.exit(0);
            }
        }
    }


}
