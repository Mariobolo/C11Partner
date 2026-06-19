package com.c11partner.desktop.utils;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * 零跑C11副屏管理类
 * 
 * 功能：
 * 1. 副屏显示管理（开关、状态查询）
 * 2. 副屏内容投屏（Presentation API）
 * 3. 副屏应用启动
 * 
 * 基于Android标准DisplayManager + Presentation API实现
 * 零跑C11副屏可能是display ID为1的副屏
 */
public class SecondaryScreenManager {
    
    private static final String TAG = "SecondaryScreenManager";
    
    private Context mContext;
    private DisplayManager mDisplayManager;
    private Presentation mPresentation;
    private boolean mIsShowing = false;
    
    // 副屏display ID（零跑C11副屏通常是display 1）
    private static final int SECONDARY_DISPLAY_ID = 1;
    
    /**
     * 构造函数
     */
    public SecondaryScreenManager(Context context) {
        mContext = context.getApplicationContext();
        mDisplayManager = (DisplayManager) mContext.getSystemService(Context.DISPLAY_SERVICE);
    }
    
    /**
     * 获取所有显示屏
     */
    public Display[] getDisplays() {
        if (mDisplayManager != null) {
            return mDisplayManager.getDisplays();
        }
        return new Display[0];
    }
    
    /**
     * 获取副屏Display对象
     */
    public Display getSecondaryDisplay() {
        Display[] displays = getDisplays();
        for (Display display : displays) {
            // 优先通过ID查找
            if (display.getDisplayId() == SECONDARY_DISPLAY_ID) {
                return display;
            }
        }
        // 找不到则返回第二个display（如果有的话）
        if (displays.length > 1) {
            return displays[1];
        }
        return null;
    }
    
    /**
     * 检查副屏是否存在
     */
    public boolean hasSecondaryDisplay() {
        return getSecondaryDisplay() != null;
    }
    
    /**
     * 获取副屏信息
     */
    public String getSecondaryDisplayInfo() {
        Display display = getSecondaryDisplay();
        if (display == null) {
            return "未检测到副屏";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("Display ID: ").append(display.getDisplayId()).append("\n");
        info.append("名称: ").append(display.getName()).append("\n");
        info.append("分辨率: ").append(display.getWidth()).append("x").append(display.getHeight()).append("\n");
        info.append("刷新率: ").append(display.getRefreshRate()).append("Hz\n");
        info.append("状态: ").append(getDisplayStateName(display.getState()));
        
        return info.toString();
    }
    
    /**
     * 获取显示状态名称
     */
    private String getDisplayStateName(int state) {
        switch (state) {
            case Display.STATE_ON:
                return "开启";
            case Display.STATE_OFF:
                return "关闭";
            case Display.STATE_DOZE:
                return "休眠";
            case Display.STATE_DOZE_SUSPEND:
                return "深度休眠";
            case Display.STATE_UNKNOWN:
            default:
                return "未知";
        }
    }
    
    /**
     * 在副屏上显示Presentation
     * 
     * @param presentation 要显示的Presentation
     * @return 是否成功显示
     */
    public boolean showPresentation(Presentation presentation) {
        try {
            Display display = getSecondaryDisplay();
            if (display == null) {
                Log.e(TAG, "未找到副屏");
                return false;
            }
            
            // 先关闭之前的
            hidePresentation();
            
            mPresentation = presentation;
            mPresentation.show();
            mIsShowing = true;
            
            Log.i(TAG, "副屏Presentation显示成功");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "显示副屏Presentation失败", e);
            return false;
        }
    }
    
    /**
     * 隐藏副屏Presentation
     */
    public void hidePresentation() {
        try {
            if (mPresentation != null && mPresentation.isShowing()) {
                mPresentation.dismiss();
            }
            mPresentation = null;
            mIsShowing = false;
        } catch (Exception e) {
            Log.e(TAG, "隐藏副屏Presentation失败", e);
        }
    }
    
    /**
     * 检查副屏是否正在显示内容
     */
    public boolean isShowing() {
        return mIsShowing && mPresentation != null && mPresentation.isShowing();
    }
    
    /**
     * 获取当前的Presentation
     */
    public Presentation getCurrentPresentation() {
        return mPresentation;
    }
    
    /**
     * 通过系统属性控制副屏开关（零跑C11专用）
     * 
     * 注意：这只是控制系统属性，实际显示还需要系统支持
     */
    public boolean setSecondaryScreenEnabled(boolean enabled) {
        try {
            // 使用CarControlManager的Settings.Global方法
            return CarControlManager.getInstance(mContext).setSecondaryScreenEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置副屏开关失败", e);
            return false;
        }
    }
    
    /**
     * 获取副屏开关状态（零跑C11专用）
     */
    public boolean isSecondaryScreenEnabled() {
        try {
            return CarControlManager.getInstance(mContext).isSecondaryScreenEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取副屏开关状态失败", e);
            return false;
        }
    }
    
    /**
     * 启动应用到副屏（需要系统支持）
     * 
     * 注意：普通应用无法直接在副屏启动其他应用
     * 需要系统级权限或特定API支持
     */
    public boolean launchAppOnSecondaryScreen(String packageName) {
        try {
            // 尝试通过Presentation方式启动
            // 普通应用无法直接在副屏启动第三方应用
            // 需要系统级支持，这里预留接口
            Log.w(TAG, "启动应用到副屏功能需要系统级权限支持");
            return false;
        } catch (Exception e) {
            Log.e(TAG, "启动应用到副屏失败", e);
            return false;
        }
    }
    
    /**
     * 释放资源
     */
    public void destroy() {
        hidePresentation();
        mDisplayManager = null;
        mContext = null;
    }
}
