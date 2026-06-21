package com.c11partner.desktop.bridge;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.service.MediaSessionService;
import com.c11partner.desktop.service.MusicNotificationListenerService;
import com.c11partner.desktop.utils.CarControlManager;
import com.c11partner.desktop.utils.MusicUtils;
import org.json.JSONObject;
/**
 * 音乐功能Bridge类
 * 处理所有音乐相关的JavaScript交互接口
 * 
 * 功能说明：
 * 1. 音乐播放控制（播放、暂停、上一首、下一首）
 * 2. 音乐状态查询（播放状态、当前歌曲、进度信息）
 * 3. 音乐可视化控制
 * 4. 系统音乐信息获取
 */
public class MusicBridge extends BaseBridge {
    private static final String TAG = "MusicBridge";
    private MainActivity mActivity;
    private MusicUtils musicUtils;
    private MediaSessionService mediaSessionService;
    private boolean isMediaSessionServiceBound = false;
    /**
     * 构造函数
     *
     * @param context  应用上下文
     * @param activity MainActivity实例
     */
    public MusicBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.mActivity = activity;
        this.musicUtils = new MusicUtils(context);
    }
    /**
     * 设置媒体会话服务
     *
     * @param service 媒体会话服务实例
     * @param isBound 是否已绑定
     */
    public void setMediaSessionService(MediaSessionService service, boolean isBound) {
        this.mediaSessionService = service;
        this.isMediaSessionServiceBound = isBound;
    }
    // ==================== 音乐可视化相关方法 ====================
    /**
     * 开始音乐可视化
     */
    @JavascriptInterface
    public void startMusicVisualizer() {
        logD(TAG, "收到启动音乐可视化的JavaScript调用");
        if (!isActivityValid()) {
            logD(TAG, "Activity无效，无法启动音乐可视化");
            return;
        }
        mActivity.runOnUiThread(() -> {
            if (mActivity.musicVisualizer != null) {
                try {
                    logD(TAG, "正在启动音乐可视化");
                    mActivity.musicVisualizer.startVisualizer();
                    logD(TAG, "音乐可视化启动完成");
                } catch (Exception e) {
                    logE(TAG, "启动音乐可视化时出错", e);
                }
            } else {
                logD(TAG, "musicVisualizer对象为空");
            }
        });
    }
    /**
     * 停止音乐可视化
     */
    @JavascriptInterface
    public void stopMusicVisualizer() {
        logD(TAG, "收到停止音乐可视化的JavaScript调用");
        if (!isActivityValid()) {
            logD(TAG, "Activity无效，无法停止音乐可视化");
            return;
        }
        mActivity.runOnUiThread(() -> {
            if (mActivity.musicVisualizer != null) {
                try {
                    mActivity.musicVisualizer.stopVisualizer();
                    logD(TAG, "音乐可视化已停止");
                } catch (Exception e) {
                    logE(TAG, "停止音乐可视化时出错", e);
                }
            }
        });
    }
    // ==================== 音乐状态查询方法 ====================
    /**
     * 检查音乐是否正在播放
     *
     * @return 音乐播放状态
     */
    @JavascriptInterface
    public boolean isMusicPlaying() {
        boolean isMusicPlaying = mActivity.isMusicPlaying;
        return isMusicPlaying;
    }
    /**
     * 获取当前音乐名称
     *
     * @return 当前音乐名称
     */
    @JavascriptInterface
    public String getCurrentMusicName() {
        if (isMediaSessionServiceBound && mediaSessionService != null) {
            return mediaSessionService.getCurrentMusicName();
        }
        return "此刻无声，佳音已备候君启...";
    }

    /**
     * 获取当前音乐艺术家
     *
     * @return 当前音乐艺术家名称
     */
    @JavascriptInterface
    public String getCurrentMusicArtist() {
        if (isMediaSessionServiceBound && mediaSessionService != null) {
            return mediaSessionService.getCurrentMusicArtist();
        }
        return "未知艺术家";
    }
    /**
     * 获取音乐播放进度信息
     *
     * @return 音乐进度信息JSON字符串
     */
    @JavascriptInterface
    public String getMusicProgressInfo() {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                JSONObject progressInfo = new JSONObject();
                progressInfo.put("isPlaying", mediaSessionService.isPlaying());
                progressInfo.put("currentPosition", mediaSessionService.getCurrentPosition());
                progressInfo.put("duration", mediaSessionService.getDuration());
                return progressInfo.toString();
            }
        } catch (Exception e) {
            logE(TAG, "获取音乐进度信息失败", e);
        }
        try {
            JSONObject defaultInfo = new JSONObject();
            defaultInfo.put("isPlaying", false);
            defaultInfo.put("currentPosition", 0);
            defaultInfo.put("duration", 0);
            return defaultInfo.toString();
        } catch (Exception e) {
            return "{\"isPlaying\":false,\"currentPosition\":0,\"duration\":0}";
        }
    }
    /**
     * 获取系统音乐信息（从通知监听服务）
     *
     * @return 音乐信息JSON字符串
     */
    @JavascriptInterface
    public String getSystemMusicInfo() {
        try {
            JSONObject musicInfo = new JSONObject();
            
            // 从通知监听服务获取音乐信息
            String title = MusicNotificationListenerService.getCurrentTitle();
            String artist = MusicNotificationListenerService.getCurrentArtist();
            boolean isPlaying = MusicNotificationListenerService.isPlaying();
            
            musicInfo.put("title", title);
            musicInfo.put("artist", artist);
            musicInfo.put("isPlaying", isPlaying);
            musicInfo.put("hasData", title != null && !title.isEmpty());
            
            return musicInfo.toString();
        } catch (Exception e) {
            logE(TAG, "获取系统音乐信息失败", e);
            try {
                JSONObject defaultInfo = new JSONObject();
                defaultInfo.put("title", "");
                defaultInfo.put("artist", "");
                defaultInfo.put("isPlaying", false);
                defaultInfo.put("hasData", false);
                return defaultInfo.toString();
            } catch (Exception ex) {
                return "{}";
            }
        }
    }
    // ==================== 音乐播放控制方法 ====================
    /**
     * 播放/暂停音乐
     */
    @JavascriptInterface
    public void playPauseMusic() {
        try {
            // 使用 CarControlManager 发送方控按键
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            // 发送播放/暂停按键（通过方控）
            // 注意：零跑C11方控可能没有播放暂停键，尝试使用媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        } catch (Exception e) {
            logE(TAG, "播放/暂停音乐失败", e);
        }
    }
    /**
     * 下一首
     */
    @JavascriptInterface
    public void nextMusic() {
        try {
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            carControl.sendNextTrack();
        } catch (Exception e) {
            logE(TAG, "下一首失败", e);
            // 降级方案：发送媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_NEXT);
        }
    }
    /**
     * 上一首
     */
    @JavascriptInterface
    public void prevMusic() {
        try {
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            carControl.sendPrevTrack();
        } catch (Exception e) {
            logE(TAG, "上一首失败", e);
            // 降级方案：发送媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }
    }
    /**
     * 播放/暂停音乐（兼容旧接口 - 已废弃，请使用playPauseMusic）
     * @deprecated 建议使用 playPauseMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playPause() {
        logD(TAG, "playPause()已废弃，建议使用playPauseMusic()");
        playPauseMusic();
    }
    /**
     * 下一首（兼容旧接口 - 已废弃，请使用nextMusic）
     * @deprecated 建议使用 nextMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playNext() {
        logD(TAG, "playNext()已废弃，建议使用nextMusic()");
        nextMusic();
    }
    /**
     * 上一首（兼容旧接口 - 已废弃，请使用prevMusic）
     * @deprecated 建议使用 prevMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playPrevious() {
        logD(TAG, "playPrevious()已废弃，建议使用prevMusic()");
        prevMusic();
    }
    /**
     * 发送媒体按钮广播
     *
     * @param keyCode 按键代码
     */
    private void sendMediaButton(int keyCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            Bundle extras = new Bundle();
            extras.putParcelable(Intent.EXTRA_KEY_EVENT, 
                new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
            intent.putExtras(extras);
            mContext.sendBroadcast(intent);
            
            // 再发送一个ACTION_UP
            Intent intentUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
            Bundle extrasUp = new Bundle();
            extrasUp.putParcelable(Intent.EXTRA_KEY_EVENT, 
                new KeyEvent(KeyEvent.ACTION_UP, keyCode));
            intentUp.putExtras(extrasUp);
            mContext.sendBroadcast(intentUp);
            
            logD(TAG, "发送媒体按钮: " + keyCode);
        } catch (Exception e) {
            logE(TAG, "发送媒体按钮失败", e);
        }
    }
    // ==================== 通知监听权限相关方法 ====================
    /**
     * 检查通知监听权限是否开启
     *
     * @return 是否已开启通知监听权限
     */
    @JavascriptInterface
    public boolean isNotificationListenerEnabled() {
        try {
            String packageName = mContext.getPackageName();
            String flat = android.provider.Settings.Secure.getString(
                mContext.getContentResolver(),
                "enabled_notification_listeners");
            if (flat != null) {
                return flat.contains(packageName);
            }
            return false;
        } catch (Exception e) {
            logE(TAG, "检查通知监听权限失败", e);
            return false;
        }
    }
    /**
     * 打开通知监听设置页面
     */
    @JavascriptInterface
    public void openNotificationListenerSettings() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            logE(TAG, "打开通知监听设置失败", e);
        }
    }
}
