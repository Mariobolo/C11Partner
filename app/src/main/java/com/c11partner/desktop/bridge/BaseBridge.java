package com.c11partner.desktop.bridge;

import android.content.Context;
import android.widget.Toast;

import com.c11partner.desktop.MainActivity;

/**
 * Bridge 基类
 * 
 * 包含所有 Bridge 共有的成员变量和工具方法
 * 
 * @author C11Partner
 * @version 1.0
 */
public class BaseBridge {
    
    protected Context mContext;
    protected MainActivity mActivity;
    
    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param activity MainActivity 实例
     */
    public BaseBridge(Context context, MainActivity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }
    
    /**
     * 在 UI 线程显示 Toast
     * 
     * @param message 要显示的消息
     */
    protected void showToastOnUiThread(final String message) {
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    /**
     * 在 UI 线程执行操作
     * 
     * @param action 要执行的操作
     */
    protected void runOnUiThread(Runnable action) {
        if (mActivity != null) {
            mActivity.runOnUiThread(action);
        }
    }
    
    /**
     * 获取上下文
     * 
     * @return Context 实例
     */
    public Context getContext() {
        return mContext;
    }
    
    /**
     * 获取 MainActivity
     * 
     * @return MainActivity 实例
     */
    public MainActivity getActivity() {
        return mActivity;
    }
}
