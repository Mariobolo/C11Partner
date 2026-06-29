package com.c11partner.desktop.bridge;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.c11partner.desktop.MainActivity;
import java.lang.FunctionalInterface;

/**
 * Bridge 基类
 * 
 * 包含所有 Bridge 共有的成员变量和工具方法
 * 提供统一的上下文管理、UI线程操作、日志输出等基础功能
 * 
 * 设计原则：
 * 1. 空安全：所有公共方法都进行空指针检查
 * 2. 简洁性：使用Lambda表达式简化Runnable创建
 * 3. 可扩展性：提供通用的工具方法供子类使用
 * 
 * @author C11Partner
 * @version 1.2
 */
public class BaseBridge {
    
    private static final String TAG = "BaseBridge";
    
    protected Context mContext;
    protected MainActivity mActivity;
    
    /**
     * 构造函数
     * 
     * @param context 上下文，不能为null
     * @param activity MainActivity 实例，可以为null
     */
    public BaseBridge(Context context, MainActivity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }
    
    /**
     * 在 UI 线程显示 Toast
     * 
     * @param message 要显示的消息，如果为null则不显示
     */
    protected void showToastOnUiThread(final String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (mActivity != null) {
            mActivity.runOnUiThread(() -> {
                if (mContext != null) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    /**
     * 在 UI 线程执行操作
     * 
     * @param action 要执行的操作，如果为null则不执行
     */
    protected void runOnUiThread(Runnable action) {
        if (action == null) {
            return;
        }
        if (mActivity != null) {
            mActivity.runOnUiThread(action);
        }
    }
    
    /**
     * 安全执行JavaScript回调
     * 
     * @param javascript JavaScript代码
     */
    protected void safeEvaluateJavascript(final String javascript) {
        if (javascript == null || javascript.isEmpty()) {
            return;
        }
        runOnUiThread(() -> {
            if (mActivity != null && mActivity.getWebView() != null) {
                try {
                    mActivity.getWebView().loadUrl(javascript);
                } catch (Exception e) {
                    Log.e(TAG, "执行JavaScript失败: " + javascript, e);
                }
            }
        });
    }
    
    /**
     * 记录调试日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    protected void logD(String tag, String message) {
        if (tag != null && message != null) {
            Log.d(tag, message);
        }
    }
    
    /**
     * 记录错误日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     * @param e 异常对象（可选）
     */
    protected void logE(String tag, String message, Exception e) {
        if (tag != null && message != null) {
            if (e != null) {
                Log.e(tag, message, e);
            } else {
                Log.e(tag, message);
            }
        }
    }
    
    /**
     * 获取上下文
     * 
     * @return Context 实例，可能为null
     */
    public Context getContext() {
        return mContext;
    }
    
    /**
     * 获取 MainActivity
     * 
     * @return MainActivity 实例，可能为null
     */
    public MainActivity getActivity() {
        return mActivity;
    }
    
    /**
     * 检查上下文是否有效
     * 
     * @return true如果上下文有效
     */
    protected boolean isContextValid() {
        return mContext != null;
    }
    
    /**
     * 检查Activity是否有效
     * 
     * @return true如果Activity有效
     */
    protected boolean isActivityValid() {
        return mActivity != null && !mActivity.isFinishing();
    }
    
    /**
     * 安全执行带返回值的操作
     * 
     * @param action 要执行的操作
     * @param defaultValue 出错时的默认返回值
     * @param <T> 返回值类型
     * @return 操作结果或默认值
     */
    protected <T> T safeExecute(Supplier<T> action, T defaultValue) {
        if (action == null) {
            return defaultValue;
        }
        try {
            return action.get();
        } catch (Exception e) {
            logE(TAG, "执行操作时出错", e);
            return defaultValue;
        }
    }
    
    /**
     * 安全执行无返回值的操作
     * 
     * @param action 要执行的操作
     */
    protected void safeExecute(Runnable action) {
        if (action == null) {
            return;
        }
        try {
            action.run();
        } catch (Exception e) {
            logE(TAG, "执行操作时出错", e);
        }
    }
    
    /**
     * 带条件检查的UI线程执行
     * 
     * @param condition 执行条件
     * @param action 要执行的操作
     */
    protected void runOnUiThreadIf(boolean condition, Runnable action) {
        if (condition && action != null) {
            runOnUiThread(action);
        }
    }
    
    /**
     * 函数式接口：支持带返回值的操作
     */
    @FunctionalInterface
    protected interface Supplier<T> {
        T get() throws Exception;
    }
}
